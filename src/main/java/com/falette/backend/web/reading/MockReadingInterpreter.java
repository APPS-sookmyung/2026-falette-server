package com.falette.backend.web.reading;

import com.falette.backend.domain.card.TarotCard;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * MVP용 목업 해석 생성기. 고정 템플릿에 고민/색/색상 선택 이유/카드 정보를 조합한다.
 * 이후 실제 LLM 연동 시 이 클래스를 교체한다.
 */
@Component
public class MockReadingInterpreter implements ReadingInterpreter {

    @Override
    public Result generate(Input in) {
        return generate(in.concernType(), in.color(), in.saturationLevel(), in.colorReason(),
                in.card(), in.question2Text());
    }

    private static final Map<String, String> CONCERN = Map.of(
            "LOVE", "연애", "STUDY", "학업", "CAREER", "직업", "WEALTH", "재물", "HEALTH", "건강");

    private static final Map<String, String> COLOR = Map.of(
            "RED", "빨강", "ORANGE", "주황", "YELLOW", "노랑", "GREEN", "초록", "BLUE", "파랑",
            "NAVY", "남색", "PURPLE", "보라", "WHITE", "흰색", "BLACK", "검정");

    private static final Map<String, String> LUCKY_ITEM = Map.of(
            "RED", "붉은 립밤", "ORANGE", "오렌지 주스", "YELLOW", "노란 메모지", "GREEN", "작은 화분",
            "BLUE", "파란 펜", "NAVY", "남색 노트", "PURPLE", "라벤더 향초", "WHITE", "흰 손수건",
            "BLACK", "검은 팔찌");

    /** 색상 선택 이유별 현재 상태 해석 */
    private static final Map<String, String> REASON_STATE = Map.of(
            "INTUITIVE", "본능과 직관에 충실하게 마음이 움직이고 있어요",
            "COMFORT", "마음을 차분히 쉬게 하고 위로받고 싶은 상태예요",
            "ENERGY", "새로운 시도와 변화를 바라는 마음이 커지고 있어요",
            "IDEAL", "닮고 싶은 모습을 향해 뚜렷한 목표를 품고 있어요",
            "PAST_MEMORY", "지난 기억과 감정을 되짚으며 마음의 연결을 소중히 여기고 있어요");

    private static final Map<String, String> REASON_KEYWORD = Map.of(
            "INTUITIVE", "직관", "COMFORT", "안정", "ENERGY", "활력", "IDEAL", "이상", "PAST_MEMORY", "추억");

    private static final Map<String, String> SATURATION_LINE = Map.of(
            "LEVEL_1", "선명한 색을 고른 만큼 지금 마음이 또렷하게 움직이고 있어요.",
            "LEVEL_2", "적당한 농도의 색처럼 마음도 균형을 찾아가는 중이에요.",
            "LEVEL_3", "은은한 색을 고른 만큼 조용히 마음을 살피고 있어요.");

    private static final Map<Integer, String> TONE_SHORT = Map.of(
            5, "눈부신 흐름이 열려 있어요.",
            4, "따뜻한 흐름이 이어지고 있어요.",
            3, "균형을 잡아가는 중이에요.",
            2, "잠시 숨을 고를 때예요.",
            1, "변화를 위한 성찰의 시간이에요.");

    private static final Map<Integer, String> TONE_BODY = Map.of(
            5, "지금은 노력과 마음이 좋은 결과로 이어지기 쉬운 시기예요. 자신감을 갖고 한 걸음 내디뎌도 좋아요.",
            4, "큰 무리 없이 좋은 방향으로 흘러가고 있어요. 지금의 마음가짐을 유지하면 더 좋아질 거예요.",
            3, "좋고 나쁨이 한쪽으로 기울지 않은 중립의 시기예요. 어떤 선택을 하느냐에 따라 흐름이 달라져요.",
            2, "서두르기보다 상황을 한 번 더 돌아보면 좋은 시기예요. 작은 신호들에 귀를 기울여 보세요.",
            1, "지금의 불편함은 더 나은 방향으로 가기 위한 정리의 과정이에요. 무겁게 생각하지 말고 나를 돌보는 시간으로 써 보세요.");

    private static final Map<Integer, String> TONE_ANSWER = Map.of(
            5, "답은 '예'에 가까워요. 망설이던 마음을 믿고 나아가도 좋아요.",
            4, "대체로 긍정적이에요. 조급해하지 않고 지금처럼 가면 좋은 소식이 따라와요.",
            3, "답은 당신의 선택에 달려 있어요. 마음이 끌리는 쪽을 차분히 살펴보세요.",
            2, "아직은 확신하기 일러요. 서두르지 말고 조금 더 지켜보며 준비해 보세요.",
            1, "당장은 쉽지 않을 수 있지만, 이 시기가 지나면 새로운 길이 보일 거예요.");

    public Result generate(String concernType, String color, String saturationLevel, String colorReason,
                           TarotCard card, String question2Text) {
        int level = card.getPositivityLevel();
        String concernLabel = CONCERN.getOrDefault(concernType, "고민");
        String colorLabel = COLOR.getOrDefault(color, "마음의 색");

        List<String> keywords = new ArrayList<>(splitKeywords(card.getKeywords()));
        keywords.add(REASON_KEYWORD.getOrDefault(colorReason, "마음"));

        String firstKeyword = keywords.get(0);
        String summary = "%s 고민에 '%s'의 카드가 찾아왔어요. %s"
                .formatted(concernLabel, firstKeyword, TONE_SHORT.get(level));

        String interpretation = String.join("\n\n",
                "당신은 %s 색을 고르며 마음속 고민을 비춰 봤어요. %s. %s".formatted(
                        colorLabel,
                        REASON_STATE.getOrDefault(colorReason, "마음의 결을 따라가고 있어요"),
                        SATURATION_LINE.getOrDefault(saturationLevel, "")).trim(),
                "%s 카드는 이렇게 말해요. %s".formatted(card.displayName(), card.getDescription()),
                TONE_BODY.get(level),
                "'%s'라는 궁금증에 대해 카드는 이렇게 답해요. %s".formatted(question2Text, TONE_ANSWER.get(level)));

        return new Result(summary, interpretation, keywords, LUCKY_ITEM.getOrDefault(color, "작은 행운 부적"));
    }

    private List<String> splitKeywords(String keywords) {
        if (keywords == null || keywords.isBlank()) {
            return List.of("새로운 흐름");
        }
        return Arrays.stream(keywords.split(",")).map(String::trim).filter(s -> !s.isEmpty()).toList();
    }
}