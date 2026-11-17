package com.falette.backend.web.reading;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
public class GeminiReadingInterpreter implements ReadingInterpreter {

    private static final Map<String, String> CONCERN = Map.of(
            "LOVE", "연애", "STUDY", "학업", "CAREER", "직업", "WEALTH", "재물", "HEALTH", "건강");
    private static final Map<String, String> COLOR = Map.of(
            "RED", "빨강", "ORANGE", "주황", "YELLOW", "노랑", "GREEN", "초록", "BLUE", "파랑",
            "NAVY", "남색", "PURPLE", "보라", "WHITE", "흰색", "BLACK", "검정");
    private static final Map<String, String> REASON = Map.of(
            "INTUITIVE", "직관적 끌림(본능과 직관에 충실한 상태)",
            "COMFORT", "안정감과 위로(마음의 휴식이 필요한 상태)",
            "ENERGY", "에너지와 변화(새로운 시도를 바라는 상태)",
            "IDEAL", "이상향과 동경(뚜렷한 목표 의식이 있는 상태)",
            "PAST_MEMORY", "과거의 기억(지난 경험과 감정을 되짚는 상태)");

    private static final String SYSTEM_PROMPT = """
            당신은 색과 타로 카드를 바탕으로 따뜻하게 마음을 읽어 주는 컬러 타로 리더입니다.
            규칙:
            - 한국어 해요체로, 다정하고 차분한 톤으로 씁니다.
            - 재미와 위로를 위한 해석입니다. 의학적 진단, 투자·재무 결정, 합격·불합격 같은 단정적 예언은 하지 않습니다.
            - 겁을 주거나 불안을 키우는 표현은 피하고, 도전적인 카드도 성찰과 방향 제시로 풀어 줍니다.
            - 카드의 의미, 고른 색과 선택 이유, 사용자가 고른 고민 상황과 궁금증을 모두 자연스럽게 엮습니다.
            - summary: 한 문장, 60자 이내.
            - interpretation: 3~4개 문단, 문단 사이는 빈 줄(\\n\\n)로 구분, 전체 500자 안팎.
            - keywords: 3~4개의 짧은 단어.
            - luckyItem: 고른 색과 어울리는 구체적인 물건 하나, 20자 이내. 음료나 차 종류는 피합니다.
            - 카드 긍정도 점수나 숫자 같은 내부 지표는 문장에 절대 언급하지 않습니다. 해석의 분위기를 정하는 참고용으로만 씁니다.
            """;

    private static final Map<String, Object> RESPONSE_SCHEMA = Map.of(
            "type", "OBJECT",
            "properties", Map.of(
                    "summary", Map.of("type", "STRING"),
                    "interpretation", Map.of("type", "STRING"),
                    "keywords", Map.of("type", "ARRAY", "items", Map.of("type", "STRING")),
                    "luckyItem", Map.of("type", "STRING")),
            "required", List.of("summary", "interpretation", "keywords", "luckyItem"));

    private final RestClient restClient;
    private final ObjectMapper objectMapper;
    private final String apiKey;
    private final String model;

    public GeminiReadingInterpreter(ObjectMapper objectMapper,
                                    @Value("${gemini.api-key:}") String apiKey,
                                    @Value("${gemini.model:gemini-flash-lite-latest}") String model,
                                    @Value("${gemini.base-url:https://generativelanguage.googleapis.com/v1beta}") String baseUrl,
                                    @Value("${gemini.timeout-seconds:20}") int timeoutSeconds) {
        this.objectMapper = objectMapper;
        this.apiKey = apiKey;
        this.model = model;
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(5_000);
        factory.setReadTimeout(timeoutSeconds * 1_000);
        this.restClient = RestClient.builder().baseUrl(baseUrl).requestFactory(factory).build();
    }

    public boolean isConfigured() {
        return apiKey != null && !apiKey.isBlank();
    }

    @Override
    public Result generate(Input in) {
        Map<String, Object> body = Map.of(
                "systemInstruction", Map.of("parts", List.of(Map.of("text", SYSTEM_PROMPT))),
                "contents", List.of(Map.of("role", "user", "parts", List.of(Map.of("text", userPrompt(in))))),
                "generationConfig", Map.of(
                        "responseMimeType", "application/json",
                        "responseSchema", RESPONSE_SCHEMA,
                        "temperature", 0.9));

        JsonNode response = call(body);

        JsonNode candidate = response == null ? null : response.path("candidates").path(0);
        String text = candidate == null ? "" : candidate.path("content").path("parts").path(0).path("text").asText("");
        if (text.isBlank()) {
            throw new IllegalStateException("Gemini 빈 응답, finishReason="
                    + (candidate == null ? "none" : candidate.path("finishReason").asText("none")));
        }
        return parse(text);
    }

    private String userPrompt(Input in) {
        return """
                고민 유형: %s
                고른 색: %s (명도 단계 %s, 채도 단계 %s, 숫자가 클수록 밝고 선명함)
                색을 고른 이유: %s
                현재 상황: %s
                궁금한 것: %s
                뽑은 카드: %s
                카드 키워드: %s
                카드 한줄 설명: %s
                카드 분위기(내부 참고용, 문장에 숫자로 쓰지 말 것): 5단계 중 %d (5가 가장 긍정, 1은 도전적·성찰)
                """.formatted(
                CONCERN.getOrDefault(in.concernType(), in.concernType()),
                COLOR.getOrDefault(in.color(), in.color()),
                levelNumber(in.brightnessLevel()), levelNumber(in.saturationLevel()),
                REASON.getOrDefault(in.colorReason(), in.colorReason()),
                in.question1Text(), in.question2Text(),
                in.card().getName(), in.card().getKeywords(), in.card().getDescription(),
                in.card().getPositivityLevel());
    }

    private String levelNumber(String level) {
        return level == null ? "?" : level.replace("LEVEL_", "");
    }

    private Result parse(String text) {
        try {
            JsonNode json = objectMapper.readTree(text);
            String summary = cut(json.path("summary").asText("").trim(), 120);
            String interpretation = json.path("interpretation").asText("").trim()
                    .replace("\r\n", "\n").replaceAll("\n{3,}", "\n\n");
            String luckyItem = cut(json.path("luckyItem").asText("").trim(), 40);

            List<String> keywords = new ArrayList<>();
            for (JsonNode k : json.path("keywords")) {
                String s = k.asText("").replace(",", " ").trim();
                if (!s.isEmpty()) keywords.add(s);
                if (keywords.size() == 5) break;
            }

            if (summary.isBlank() || interpretation.isBlank() || keywords.isEmpty() || luckyItem.isBlank()) {
                throw new IllegalStateException("Gemini 응답 필드 누락");
            }
            return new Result(summary, interpretation, keywords, luckyItem);
        } catch (IllegalStateException e) {
            throw e;
        } catch (Exception e) {
            throw new IllegalStateException("Gemini 응답 파싱 실패", e);
        }
    }

    private String cut(String s, int max) {
        return s.length() <= max ? s : s.substring(0, max);
    }

    private JsonNode call(Map<String, Object> body) {
        final int maxAttempts = 3;
        for (int attempt = 1; ; attempt++) {
            try {
                return restClient.post()
                        .uri("/models/{model}:generateContent", model)
                        .header("x-goog-api-key", apiKey)
                        .body(body)
                        .retrieve()
                        .body(JsonNode.class);
            } catch (HttpServerErrorException | ResourceAccessException e) {
                if (attempt >= maxAttempts) {
                    throw e;
                }
                log.warn("Gemini 일시 오류({}), {}번째 재시도합니다", e.getClass().getSimpleName(), attempt);
                try {
                    Thread.sleep(attempt * 1000L);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw e;
                }
            }
        }
    }
}