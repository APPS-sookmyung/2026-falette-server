# Falette Backend

컬러 심리 x 타로 AI 리딩 서비스 Falette의 Spring Boot 백엔드 프로젝트입니다.

## 스택
- Java 21, Spring Boot 3.3.5
- Spring Web, Spring Data JPA, Spring Security + OAuth2 Client(구글 로그인, MVP 이후 적용 예정)
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

## 패키지 구조
```
com.falette.backend
├── config          # SecurityConfig, WebClientConfig
├── common          # BaseTimeEntity (createdAt 자동 기록)
├── domain
│   ├── user        # User, AuthProvider(GUEST/GOOGLE)
│   ├── reading      # Reading(핵심 엔티티) + ConcernType/ColorOption/ColorLevel/ColorReason enum
│   └── card         # TarotCard
└── web              # REST 컨트롤러
```