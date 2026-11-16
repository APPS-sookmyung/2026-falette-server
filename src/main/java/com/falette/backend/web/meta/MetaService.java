package com.falette.backend.web.meta;

import com.falette.backend.domain.card.TarotCardRepository;
import com.falette.backend.domain.loading.LoadingMessage;
import com.falette.backend.domain.loading.LoadingMessageRepository;
import com.falette.backend.web.meta.dto.LoadingMessagesResponse;
import com.falette.backend.web.meta.dto.MetaOptionsResponse;
import com.falette.backend.web.meta.dto.TarotCardSummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MetaService {

    private final TarotCardRepository tarotCardRepository;
    private final LoadingMessageRepository loadingMessageRepository;

    public MetaOptionsResponse getOptions() {
        return MetaOptionsResponse.defaults();
    }

    public LoadingMessagesResponse getLoadingMessages() {
        List<String> messages = loadingMessageRepository.findAll(Sort.by("id")).stream()
                .map(LoadingMessage::getText)
                .toList();
        return new LoadingMessagesResponse(messages);
    }

    public List<TarotCardSummaryResponse> getTarotCards() {
        return tarotCardRepository.findAll(Sort.by("id")).stream()
                .map(TarotCardSummaryResponse::from)
                .toList();
    }
}