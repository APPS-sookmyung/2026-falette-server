package com.falette.backend.web.reading;

import com.falette.backend.domain.card.TarotCard;

import java.util.List;

/** 리딩 해석 생성기. 목업/LLM 구현을 갈아끼울 수 있게 분리한 규격. */
public interface ReadingInterpreter {

    Result generate(Input input);

    record Input(String concernType, String color, String brightnessLevel, String saturationLevel,
                 String colorReason, TarotCard card, String question1Text, String question2Text) {
    }

    record Result(String summary, String interpretation, List<String> keywords, String luckyItem) {
    }
}