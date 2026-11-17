package com.falette.backend.web.reading;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

/** Gemini를 먼저 시도하고, 키가 없거나 실패하면 목업으로 대체한다. 서비스는 이 빈을 주입받는다. */
@Slf4j
@Primary
@Component
@RequiredArgsConstructor
public class FallbackReadingInterpreter implements ReadingInterpreter {

    private final GeminiReadingInterpreter gemini;
    private final MockReadingInterpreter mock;

    @Override
    public Result generate(Input input) {
        if (!gemini.isConfigured()) {
            return mock.generate(input);
        }
        try {
            return gemini.generate(input);
        } catch (Exception e) {
            log.warn("Gemini 해석 실패, 목업으로 대체합니다: {}", e.toString());
            return mock.generate(input);
        }
    }
}