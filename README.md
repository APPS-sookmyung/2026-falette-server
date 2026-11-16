# Falette Backend

컬러 심리 x 타로 AI 리딩 서비스 Falette의 Spring Boot 백엔드 프로젝트입니다.

## 스택
- Java 21, Spring Boot 3.3.5
- Spring Web, Spring Data JPA, Spring Security + OAuth2 Client(구글 로그인)
- PostgreSQL (로컬은 Docker Compose로 실행)
- AI: MVP는 OpenAI GPT-4o-mini로 시작 예정

## 실행 방법
1. PostgreSQL 실행
```bash
   docker compose up -d
```
2. Gradle Wrapper 생성 (최초 1회)
```bash
   gradle wrapper --gradle-version 8.10
```
3. 서버 실행
```bash
   ./gradlew bootRun
```

기본 프로필은 `local`이고, `docker-compose.yml`로 띄운 PostgreSQL(`localhost:5432/falette`, 계정 `falette`/`falette`)에 연결됩니다.

헬스체크: `GET /api/health`

## 인증 방식
- 비회원: 프론트가 만든 UUID를 `X-Guest-UUID` 헤더로 전송 → 처음 보낸 UUID는 자동으로 비회원 사용자로 등록
- 구글 회원: `GET /oauth2/authorization/google`로 이동 → 로그인 후 `JSESSIONID` 세션 쿠키 (프론트는 `credentials: 'include'` 필요)
- 우선순위: 로그인 세션 → `X-Guest-UUID` → 둘 다 없으면 401
- 컨트롤러에서는 파라미터에 `@CurrentUser User user`만 붙이면 현재 사용자가 들어옴

구글 로그인을 로컬에서 테스트하려면 `GOOGLE_CLIENT_ID`, `GOOGLE_CLIENT_SECRET` 환경 변수를 넣고 `local,oauth` 프로필로 실행합니다.
설정이 없으면 구글 로그인만 꺼진 채로 서버가 뜹니다 (비회원 흐름은 그대로 동작).

## 구현된 API
| Method | Path | 설명 |
| --- | --- | --- |
| GET | `/api/health` | 헬스체크 |
| GET | `/oauth2/authorization/google` | 구글 로그인 진입 (Spring Security) |
| GET | `/api/v1/auth/me` | 현재 사용자 상태 (구글/비회원) |
| POST | `/api/v1/auth/logout` | 로그아웃 (세션 무효화) |
| GET | `/api/v1/mypage/calendar?year=2026&month=9` | 월별 캘린더 (날짜별 색, 하루 여러 번이면 마지막 기록) |
| GET | `/api/v1/tarot/readings/{readingId}` | 리딩 상세 (본인 기록만, 아니면 404) |

에러 응답 형식: `{ "status": 401, "code": "UNAUTHORIZED", "message": "..." }`

## 패키지 구조
```
com.falette.backend
├── auth            # @CurrentUser, 사용자 식별(세션→게스트 헤더), 구글 OAuth2 사용자 처리
├── config          # SecurityConfig, WebConfig, AppProperties
├── common          # BaseTimeEntity, 공통 예외/에러 응답
├── domain
│   ├── user        # User(APP_USER), AuthProvider(GUEST/GOOGLE)
│   ├── reading     # Reading + ConcernType/ColorOption/ColorLevel/ColorReason enum
│   └── card        # TarotCard
└── web             # REST 컨트롤러 (auth, mypage, reading, health)
```
