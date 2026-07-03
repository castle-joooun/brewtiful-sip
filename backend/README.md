# brewtiful-sip — backend

홈카페 QR 주문 서비스 백엔드 (Spring Boot 3, Java 21). Phase 1 MVP.
설계 문서: [`../docs`](../docs) (기능명세서 / ERD / api-docs / decisions).

## 요구 사항

- JDK 21 (LTS)
- Docker / Docker Compose (로컬 MySQL)

## 최초 셋업 (Gradle wrapper 생성)

이 저장소에는 `gradle-wrapper.jar` 바이너리를 커밋하지 않았습니다. 최초 1회 아래로 생성하세요.

```bash
# gradle이 설치돼 있다면 (예: sdkman: `sdk install gradle 8.10.2`)
cd backend
gradle wrapper --gradle-version 8.10.2
```

이후에는 `./gradlew` 로 실행합니다.

## 실행

```bash
# 1) 로컬 MySQL 기동
docker compose -f ../infra/docker-compose.yml up -d

# 2) 마스터 코드(운영자 인증, ADR-0003) 환경변수로 주입 후 앱 실행
export BREWTIFUL_MASTER_CODE=change-me-local
cd backend
./gradlew bootRun
```

프로파일: 기본 `local`(MySQL), 테스트 `test`(H2 / Testcontainers).

## 테스트

```bash
cd backend
./gradlew test
```
