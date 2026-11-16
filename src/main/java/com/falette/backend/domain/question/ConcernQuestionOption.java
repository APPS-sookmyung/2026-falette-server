package com.falette.backend.domain.question;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "concern_question_option")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConcernQuestionOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "question_id", nullable = false)
    private ConcernQuestion question;

    @Column(name = "option_order", nullable = false)
    private int optionOrder;

    @Column(name = "option_text", nullable = false, length = 300)
    private String optionText;

    static ConcernQuestionOption create(ConcernQuestion question, int optionOrder, String optionText) {
        ConcernQuestionOption o = new ConcernQuestionOption();
        o.question = question;
        o.optionOrder = optionOrder;
        o.optionText = optionText;
        return o;
    }
}