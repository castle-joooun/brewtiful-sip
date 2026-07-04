-- brewtiful-sip 초기 시드 데이터 (Phase 1)
-- 원두(bean)/메뉴(menu)는 Phase 1에서 관리자 API 없이 이 SQL로 관리한다.
--   - 원두: ADR-0002 (DB 단일 출처 + GET /beans 조회 API). 품절/노출은 active 플래그.
--   - 메뉴: 기능명세서 3.7 (SQL 또는 최소 관리자 API로 관리).
-- 스키마 컬럼명은 docs/ERD.md 기준(snake_case). 실제 DDL은 스캐폴딩 단계에서 JPA/Flyway로 생성.
-- 한글 깨짐 방지: 임포트 세션 charset을 utf8mb4로 고정한다.
SET NAMES utf8mb4;

-- ─────────────────────────────────────────────
-- Bean (원두)
-- ─────────────────────────────────────────────
INSERT INTO bean (name, origin, roast_level, process, flavor_notes, active, created_at, updated_at) VALUES
  ('에티오피아 예가체프', '에티오피아', '미디엄',      '워시드',  '자스민, 레몬, 홍차',        true,  NOW(), NOW()),
  ('콜롬비아 수프리모',   '콜롬비아',   '미디엄다크',  '워시드',  '초콜릿, 캐러멜, 견과류',    true,  NOW(), NOW()),
  ('과테말라 안티구아',   '과테말라',   '미디엄',      '워시드',  '다크초콜릿, 오렌지, 스모키', true,  NOW(), NOW()),
  ('케냐 AA',             '케냐',       '미디엄',      '워시드',  '블랙커런트, 자몽, 와이니',   false, NOW(), NOW()); -- 품절 예시

-- ─────────────────────────────────────────────
-- Menu (메뉴)
--   requires_bean=true 인 메뉴만 주문 시 bean_id 선택 필요
-- ─────────────────────────────────────────────
INSERT INTO menu (name, price, requires_bean, active, created_at, updated_at) VALUES
  ('아이스 아메리카노', 4000, false, true, NOW(), NOW()),
  ('따뜻한 아메리카노', 4000, false, true, NOW(), NOW()),
  ('브루잉 커피',       5500, true,  true, NOW(), NOW()),  -- 원두 선택 필요
  ('카페 라떼',         5000, false, true, NOW(), NOW());
