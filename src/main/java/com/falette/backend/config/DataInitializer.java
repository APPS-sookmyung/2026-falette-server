package com.falette.backend.config;

import com.falette.backend.domain.card.TarotCard;
import com.falette.backend.domain.card.TarotCardRepository;
import com.falette.backend.domain.loading.LoadingMessage;
import com.falette.backend.domain.loading.LoadingMessageRepository;
import com.falette.backend.domain.question.ConcernQuestion;
import com.falette.backend.domain.question.ConcernQuestionOption;
import com.falette.backend.domain.question.ConcernQuestionRepository;
import com.falette.backend.domain.reading.ConcernType;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * 마스터 데이터 시드 (타로 카드 20장, 로딩 멘트 10개, 고민 질문 트리).
 * 각 테이블이 비어 있을 때만 넣기 때문에 서버를 재시작해도 중복되지 않는다.
 */
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final TarotCardRepository tarotCardRepository;
    private final LoadingMessageRepository loadingMessageRepository;
    private final ConcernQuestionRepository concernQuestionRepository;

    @Override
    @Transactional
    public void run(String... args) {
        seedTarotCards();
        seedLoadingMessages();
        seedConcernQuestions();
    }

    // ---------------------------------------------------------------- 타로 카드

    private void seedTarotCards() {
        if (tarotCardRepository.count() > 0) {
            return;
        }
        // positivityLevel 매핑은 초안이며 팀 확정 필요. 이미지 경로와 한줄 설명도 임시값.
        card("The Sun (태양)", "the-sun", 5, "환한 빛이 비추듯 모든 일이 밝게 풀려가는 시기예요.", "밝음,생명력");
        card("The Star (별)", "the-star", 5, "어둠 속에서도 희망의 빛이 길을 안내해 주고 있어요.", "희망,영감");
        card("The World (세계)", "the-world", 5, "한 단계를 훌륭히 마무리하고 완성에 다다랐어요.", "완성,통합");
        card("Judgement (심판)", "judgement", 5, "지난 시간을 정리하고 새롭게 깨어나는 순간이에요.", "각성,재탄생");

        card("The Empress (여황제)", "the-empress", 4, "풍요와 따뜻함이 자라나는 넉넉한 시기예요.", "풍요,자연");
        card("The Lovers (연인)", "the-lovers", 4, "마음이 통하는 조화로운 인연과 선택의 순간이에요.", "사랑,조화");
        card("Strength (힘)", "strength", 4, "부드러운 용기가 가장 큰 힘이 되어주는 때예요.", "용기,내적 힘");
        card("The Fool (광대)", "the-fool", 4, "가벼운 마음으로 새로운 첫걸음을 내딛기 좋은 때예요.", "자유,새로운 시작");

        card("Wheel of Fortune (운명의 수레바퀴)", "wheel-of-fortune", 3, "운의 흐름이 돌고 있어요, 변화의 순환에 올라타 보세요.", "순환,변화");
        card("Temperance (절제)", "temperance", 3, "서로 다른 것들이 어우러지는 조화의 시기예요.", "조화,균형");
        card("Justice (정의)", "justice", 3, "균형 잡힌 시선으로 바라보면 진실이 보여요.", "균형,진실");
        card("The Chariot (전차)", "the-chariot", 3, "굳은 의지로 방향을 잡으면 앞으로 나아갈 수 있어요.", "의지,돌파");

        card("The Moon (달)", "the-moon", 2, "아직 보이지 않는 마음과 직관에 귀 기울여 보세요.", "신비,무의식");
        card("The Emperor (황제)", "the-emperor", 2, "흔들림 없는 기준과 책임감으로 중심을 잡아야 해요.", "권력,안정");
        card("The High Priestess (여사제)", "the-high-priestess", 2, "말하지 않은 진실을 직관으로 읽어내야 하는 때예요.", "직관,비밀");
        card("The Hanged Man (매달린 남자)", "the-hanged-man", 2, "잠시 멈추고 다른 각도에서 바라보면 답이 보여요.", "정지,새로운 시각");

        card("The Devil (악마)", "the-devil", 1, "나를 붙잡고 있는 집착을 알아차리면 자유로워질 수 있어요.", "욕망,속박");
        card("Death (죽음)", "death", 1, "하나의 끝이 새로운 시작으로 이어지는 변화의 문이에요.", "변화,끝과 시작");
        card("The Tower (탑)", "the-tower", 1, "갑작스러운 변화가 낡은 것을 정리하고 새 길을 열어줘요.", "파괴,급변");
        card("The Hermit (은둔자)", "the-hermit", 1, "잠시 멈추고 내면의 등불을 따라가 보는 시간이에요.", "고독,내면 탐구");
    }

    private void card(String name, String slug, int level, String description, String keywords) {
        tarotCardRepository.save(
                TarotCard.create(name, "/images/tarot/" + slug + ".png", level, description, keywords));
    }

    // ---------------------------------------------------------------- 로딩 멘트

    private void seedLoadingMessages() {
        if (loadingMessageRepository.count() > 0) {
            return;
        }
        List.of(
                "우주의 기운을 모으는 중이에요",
                "당신의 마음에 한 걸음 더 가까워지는 중...",
                "흩어진 운명의 조각들을 하나씩 모으고 있어요.",
                "당신의 색이 카드를 만드는 중...",
                "오늘의 감정이 타로 속으로 스며들고 있어요",
                "마음의 색을 읽어내는 중입니다",
                "당신의 빛깔에 맞는 타로 카드가 모습을 드러내고 있어요",
                "마음속 깊은 곳의 컬러 신호를 포착하고 있습니다",
                "흩어진 감정의 오로라를 하나의 문장으로 정돈하는 중...",
                "마음의 결을 따라 오늘의 컬러를 물들이고 있어요"
        ).forEach(text -> loadingMessageRepository.save(LoadingMessage.create(text)));
    }

    // ---------------------------------------------------------------- 고민 질문 트리

    private void seedConcernQuestions() {
        if (concernQuestionRepository.count() > 0) {
            return;
        }

        concern(ConcernType.LOVE,
                "현재 당신 눈 앞의 상황은 어떤가요?",
                s("새로운 인연을 만나고 싶어요", "마음에 두고 있는 사람이 있어요", "연애 중이에요",
                        "재회를 고민하고 있어요", "헤어짐을 받아들이고 있어요"),
                s("현재 당신이 궁금한 것은 무엇인가요?", "현재 당신이 궁금한 것은 무엇인가요?",
                        "현재 당신이 궁금한 것은 무엇인가요?", "현재 당신이 궁금한 것은 무엇인가요?",
                        "현재 당신이 궁금한 것은 무엇인가요?"),
                new String[][]{
                        s("조만간 나에게 새로운 사랑이 찾아올까요?",
                                "다른 사람이 보는 나의 매력과 연애 스타일은 무엇일까요?",
                                "내가 연애를 시작하기 위해 고쳐야 할 점은 무엇일까요?",
                                "연애를 할 때 내가 가장 중요하게 생각하는 가치는 무엇일까요?",
                                "인연은 내 주변에 있을까요, 새로운 사람일까요?"),
                        s("상대방의 숨겨진 진짜 속마음은 무엇일까요?",
                                "우리가 연인으로 발전할 수 있는 가능성이 있을까요?",
                                "내가 먼저 다가가는 게 좋을까요, 기다리는 게 좋을까요?",
                                "상대방이 지금 연애를 할 준비가 되어 있는 상태인가요?",
                                "우리 관계를 가로막고 있는 장애물과 해결 방안은 무엇일까요?"),
                        s("우리의 앞날은 앞으로 어떻게 흘러가게 될까요?",
                                "최근 우리 사이에 생긴 갈등의 해결책은 무엇일까요?",
                                "상대방이 나에게 차마 말하지 못한 속마음이 있나요?",
                                "우리가 서로에게 정말 잘 맞는 운명적인 인연일까요?",
                                "더 행복한 관계를 위해 내가 노력해야 할 부분은 무엇일까요?"),
                        s("헤어진 그 사람과 다시 만날 운명인가요?",
                                "상대방은 지금 나를 그리워하고 있을까요?",
                                "먼저 연락을 해도 될까요? 아니면 기다려야 할까요?",
                                "다시 만난다면 이전의 문제를 극복할 수 있을까요?",
                                "그 사람이 생각하는 우리의 결정적인 이별 원인은 무엇일까요?"),
                        s("이 인연을 이제는 정말 정리하는 게 맞는 선택일까요?",
                                "이별의 아픔을 딛고 새로운 사랑을 시작해도 될까요?",
                                "이번 이별이 나에게 주는 진정한 의미는 무엇일까요?",
                                "앞으로 나를 더 아껴줄 다음 인연은 어떤 사람일까요?",
                                "현재의 슬픔을 빨리 극복하기 위해 필요한 조언은 무엇일까요?")
                });

        concern(ConcernType.STUDY,
                "당신이 학업을 통해 성취하고자 하는 것은 무엇인가요?",
                s("압도적인 성취와 인정을 원해요", "실질적인 성장을 하고 싶어요", "안정적인 미래를 희망해요",
                        "배움의 즐거움을 느끼고 싶어요", "성실하게 완수하고 싶어요"),
                s("성취를 이루기 위해, 당신이 현재 가장 필요하다고 느끼는 무기는 무엇인가요?",
                        "본인이 생각하는 '진정한 실력자'가 되기 위해 지금 가장 집중하고 있는 과정은?",
                        "미래의 안정을 준비하는 지금, 당신의 마음을 가장 불안하게 만드는 요소는 무엇인가요?",
                        "배움을 통해 지금 가장 얻고 싶은 것은 무엇인가요?",
                        "오늘도 자신과의 약속을 지켜내기 위해 가장 경계하고 있는 마음은 무엇인가요?"),
                new String[][]{
                        s("강력한 집중력을 갖고 싶어요", "자기관리가 필요해요", "빠른 정보력을 얻고 싶어요",
                                "강철 멘탈을 갖고 싶어요", "든든한 조력자가 필요해요"),
                        s("기초를 재확립하고 싶어요", "실전 경험을 축적하고 싶어요", "비판적 사고를 하고 싶어요",
                                "한계를 극복하고 싶어요", "전문성을 키우고 싶어요"),
                        s("지금 선택에 대한 확신이 서지 않아요", "남들보다 뒤처질까 걱정돼요",
                                "경제적/현실적 제약이 걸려요", "선택의 기로에 놓여 있어요", "체력적 한계를 느꼈어요"),
                        s("영감을 얻고 싶어요", "시야를 확장하고 싶어요", "깊이 있는 대화를 나누고 싶어요",
                                "나만의 결과물을 만들고 싶어요", "몰입을 즐기고 싶어요"),
                        s("포기하고 싶은 유혹을 떨치고 싶어요", "결과에 대한 조급함을 버리고 싶어요",
                                "타인의 시선을 신경쓰고 싶지 않아요", "번아웃의 전조를 느끼고 있어요",
                                "익숙함의 함정에 빠지고 싶지 않아요")
                });

        concern(ConcernType.CAREER,
                "현재 어떤 상황에 가장 가깝나요?",
                s("취업 준비 중이에요", "새로운 환경을 원해요", "현재 직장 생활 중이에요",
                        "진로를 정하지 못한 상태예요", "창업 또는 프리랜서 고민 중이에요"),
                s("취업 준비 중에서 지금 가장 신경 쓰이는 부분은 무엇인가요?",
                        "새로운 환경을 고민하게 된 가장 큰 이유는 무엇인가요?",
                        "현재 직장 생활에서 가장 크게 느끼는 문제는 무엇인가요?",
                        "진로를 정하지 못하는 가장 큰 이유는 무엇인가요?",
                        "창업 또는 프리랜서를 고민할 때 가장 걱정되는 부분은 무엇인가요?"),
                new String[][]{
                        s("지원할 직무를 정하는 게 어려워요", "서류나 포트폴리오 준비가 막막해요", "면접에 대한 부담감이 커요",
                                "합격 가능성에 대한 불안감이 커요", "준비 기간이 길어지는 게 걱정돼요"),
                        s("현재 업무가 나와 맞지 않아요", "직장 내 인간관계가 힘들어요", "성장 가능성이 부족해요",
                                "연봉이나 처우가 불만족스러워요", "번아웃이나 스트레스가 심해요"),
                        s("업무에 대한 스트레스가 커요", "상사 또는 동료와의 관계가 고민이에요",
                                "성장 가능성이 부족하다고 느껴져요", "성과나 인정에 대한 고민이 있어요",
                                "연봉이나 처우가 불만이에요"),
                        s("내가 잘하는 것이 무엇인지 모르겠어요", "좋아하는 일과 현실적인 일 사이에 벽이 있어요",
                                "선택지가 너무 많아서 혼란스러워요", "실패가 두려워요", "주변의 기대가 부담돼요"),
                        s("수입이 안정적이지 않을까 봐 걱정이에요", "혼자 일을 관리해야 하는 것이 부담이에요",
                                "고객이나 일을 구하는 문제가 걱정돼요", "내가 가진 역량이 충분한지 궁금해요",
                                "시작할 타이밍이 맞는지 모르겠어요")
                });

        concern(ConcernType.WEALTH,
                "현재 당신의 경제적 흐름은 어떠한가요?",
                s("수입의 양을 늘리고 싶어요", "지출 관리를 하고 싶어요", "투자 결정을 앞두고 있어요",
                        "미래 준비를 하고 있어요", "흐름을 회복하고 싶어요"),
                s("수입을 늘리기 위해 당신이 가장 주목하고 있는 통로는 무엇인가요?",
                        "현재 당신의 돈이 가장 무의미하게 새어나가는 곳은 어디인가요?",
                        "이번 투자나 계약에서 당신이 가장 중요하게 생각하는 판단 기준은?",
                        "미래를 위해 어떤 준비를 시작하고 싶으신가요?",
                        "자산의 선순환을 위해 가장 먼저 해결하고 싶은 과제는 무엇인가요?"),
                new String[][]{
                        s("현재 직장에서 연봉과 보상을 높이고 싶어요", "자투리 시간을 활용해 새로운 부수입을 만들고 싶어요",
                                "스스로 돈 버는 수익 시스템을 만들고 싶어요", "사람들을 만나 새로운 기회를 잡고 싶어요",
                                "미래의 내 가치를 높이는 데에 투자하고 싶어요"),
                        s("쇼핑, 배달 음식 등에 충동적으로 소비해요", "고정 비용들을 정리하고 싶어요",
                                "경조사비나 모임비가 부담돼요", "금융에 대한 정보나 지식이 부족해요",
                                "게임, 취미 등 한 곳에 중독적으로 지출해요"),
                        s("원금을 잃지 않는 것이 최우선이에요", "리스크를 감수하더라도 높은 수익을 원해요",
                                "전문가의 조언이나 시장의 흐름을 믿고 싶어요", "나의 직감과 결단력을 믿고 싶어요",
                                "당장의 이익보다는 미래의 가치를 기대하고 있어요"),
                        s("돈에 대한 기초 지식부터 쌓고 싶어요", "돈이 들어올 수 있는 환경을 만들고 싶어요",
                                "돈을 대하는 작은 습관부터 고쳐보려 해요", "롤모델을 찾거나 강의를 들으며 방향을 설정하고 싶어요",
                                "불안감을 떨쳐내고 편안한 마음가짐을 갖고 싶어요"),
                        s("심리적인 압박감에서 벗어나고 싶어요", "나의 정당한 몫을 돌려받고 싶어요",
                                "새로운 곳으로 자금을 옮기고 싶어요", "복잡하게 얽힌 세금이나 행정적인 절차들을 마무리 짓고 싶어요",
                                "잘못된 돈 관리 방식을 정리하고 처음부터 다시 시작하고 싶어요")
                });

        concern(ConcernType.HEALTH,
                "현재 어떤 건강 상태에 가장 가까우신가요?",
                s("몸이 쉽게 피로해지는 상태예요", "스트레스나 불안이 큰 상태예요", "생활 습관이 흐트러진 상태예요",
                        "회복이나 관리가 필요한 상태예요", "전반적인 건강운이 궁금한 상태예요"),
                s("피로감을 가장 크게 느끼는 순간은 언제인가요?",
                        "스트레스로 인한 어려움은 무엇인가요?",
                        "가장 먼저 정리하고 싶은 생활 습관은 무엇인가요?",
                        "지금 가장 신경 쓰고 싶은 관리 방향은 무엇인가요?",
                        "건강과 관련해 가장 알고 싶은 흐름은 무엇인가요?"),
                new String[][]{
                        s("아침에 일어날 때 가장 피곤해요", "공부나 일을 하는 중간에 지쳐요", "저녁이 될수록 점점 힘이 빠져요",
                                "사람을 만나고 오면 기운이 없어요", "특별한 이유 없이 계속 피곤해요"),
                        s("잠을 잘 못 자고 있어요", "식욕이 급변했어요", "집중이 잘 되지 않아요",
                                "감정 기복이 커졌어요", "몸이 긴장되거나 답답해요"),
                        s("수면 패턴을 규칙적으로 바꾸고 싶어요", "식사 습관을 교정하고 싶어요", "꾸준히 운동을 하고 싶어요",
                                "스마트폰 사용 시간을 줄이고 싶어요", "휴식 없이 무리하는 습관을 고치고 싶어요"),
                        s("체력 회복에 힘쓰고 싶어요", "면역력 관리가 필요해요", "통증이나 불편감을 완화하고 싶어요",
                                "마음의 안정을 찾고 싶어요", "규칙적인 생활을 하고 싶어요"),
                        s("앞으로 컨디션이 좋아질지 궁금해요", "조심해야 할 부분이 있는지 알고 싶어요",
                                "지금 생활 방식이 괜찮은지 궁금해요", "나에게 필요한 휴식 방식을 알고 싶어요",
                                "건강을 위해 바꿔야 할 습관이 무엇인지 궁금해요")
                });
    }

    /**
     * 한 고민 유형의 질문 트리를 저장한다.
     * 1차 질문(상황 선택)을 먼저 저장해 선택지 id를 만든 뒤, 선택지마다 2차 질문을 연결한다.
     */
    private void concern(ConcernType type, String primaryText, String[] situations,
                         String[] secondaryTexts, String[][] secondaryOptions) {
        ConcernQuestion primary = ConcernQuestion.create(type, 1, primaryText, null);
        List<ConcernQuestionOption> primaryOptions = new ArrayList<>();
        for (int i = 0; i < situations.length; i++) {
            primaryOptions.add(primary.addOption(i + 1, situations[i]));
        }
        concernQuestionRepository.save(primary);

        for (int i = 0; i < situations.length; i++) {
            ConcernQuestion secondary = ConcernQuestion.create(type, 2, secondaryTexts[i], primaryOptions.get(i));
            for (int j = 0; j < secondaryOptions[i].length; j++) {
                secondary.addOption(j + 1, secondaryOptions[i][j]);
            }
            concernQuestionRepository.save(secondary);
        }
    }

    private static String[] s(String... values) {
        return values;
    }
}