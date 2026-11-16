package com.falette.backend.domain.question;

import com.falette.backend.domain.reading.ConcernType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "concern_question")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ConcernQuestion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(name = "concern_type", nullable = false, length = 30)
    private ConcernType concernType;

    /** 1 = 1차(상황 선택), 2 = 2차(상황별 세부 질문) */
    @Column(name = "question_order", nullable = false)
    private int questionOrder;

    @Column(name = "question_text", nullable = false, length = 300)
    private String questionText;

    /** 2차 질문일 때만 값 있음: 어떤 1차 선택지에 딸린 질문인지. 1차 질문은 null */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_option_id")
    private ConcernQuestionOption parentOption;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL)
    @OrderBy("optionOrder ASC")
    private List<ConcernQuestionOption> options = new ArrayList<>();

    public static ConcernQuestion create(ConcernType concernType, int questionOrder,
                                         String questionText, ConcernQuestionOption parentOption) {
        ConcernQuestion q = new ConcernQuestion();
        q.concernType = concernType;
        q.questionOrder = questionOrder;
        q.questionText = questionText;
        q.parentOption = parentOption;
        return q;
    }

    public ConcernQuestionOption addOption(int optionOrder, String optionText) {
        ConcernQuestionOption option = ConcernQuestionOption.create(this, optionOrder, optionText);
        this.options.add(option);
        return option;
    }
}