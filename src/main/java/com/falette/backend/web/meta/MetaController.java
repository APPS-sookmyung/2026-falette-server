package com.falette.backend.web.meta;

import com.falette.backend.web.meta.dto.LoadingMessagesResponse;
import com.falette.backend.web.meta.dto.MetaOptionsResponse;
import com.falette.backend.web.meta.dto.TarotCardSummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/** 마스터 메타데이터 API. 질문 트리는 ConcernQuestionController(/meta/concerns). */
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/meta")
public class MetaController {

    private final MetaService metaService;

    @GetMapping("/options")
    public MetaOptionsResponse getOptions() {
        return metaService.getOptions();
    }

    @GetMapping("/loading-messages")
    public LoadingMessagesResponse getLoadingMessages() {
        return metaService.getLoadingMessages();
    }

    @GetMapping("/tarot-cards")
    public List<TarotCardSummaryResponse> getTarotCards() {
        return metaService.getTarotCards();
    }
}