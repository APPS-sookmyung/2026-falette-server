package com.falette.backend.web.question;

import com.falette.backend.domain.reading.ConcernType;
import com.falette.backend.web.question.dto.ConcernQuestionTreeResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/meta/concerns")
public class ConcernQuestionController {

    private final ConcernQuestionService concernQuestionService;

    /** 고민 구체화 화면용: 1차 질문 + 상황별 2차 질문 전체 트리 */
    @GetMapping("/{concernType}/questions")
    public List<ConcernQuestionTreeResponse> getQuestions(@PathVariable ConcernType concernType) {
        return concernQuestionService.getTree(concernType);
    }
}