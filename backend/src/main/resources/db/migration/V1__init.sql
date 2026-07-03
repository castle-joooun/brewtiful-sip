-- V1: 초기 스키마 (Phase 1). 참고: docs/ERD.md
-- 주의: `order`는 MySQL 예약어이므로 주문 테이블명은 `orders`를 사용한다.

CREATE TABLE bean (
    id           BIGINT       NOT NULL AUTO_INCREMENT,
    name         VARCHAR(100) NOT NULL,
    origin       VARCHAR(100),
    roast_level  VARCHAR(50),
    process      VARCHAR(50),
    flavor_notes VARCHAR(255),
    active       BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at   DATETIME(6)  NOT NULL,
    updated_at   DATETIME(6)  NOT NULL,
    PRIMARY KEY (id),
    KEY idx_bean_active (active)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE menu (
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    name          VARCHAR(100) NOT NULL,
    price         INT          NOT NULL,
    requires_bean BOOLEAN      NOT NULL DEFAULT FALSE,
    active        BOOLEAN      NOT NULL DEFAULT TRUE,
    created_at    DATETIME(6)  NOT NULL,
    updated_at    DATETIME(6)  NOT NULL,
    PRIMARY KEY (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE orders (
    id          BIGINT      NOT NULL AUTO_INCREMENT,
    order_token CHAR(36)    NOT NULL,
    status      VARCHAR(20) NOT NULL,
    ready_at    DATETIME(6) NULL,
    created_at  DATETIME(6) NOT NULL,
    updated_at  DATETIME(6) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_orders_order_token (order_token),
    KEY idx_orders_status (status)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE order_item (
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    order_id   BIGINT       NOT NULL,
    menu_id    BIGINT       NOT NULL,
    bean_id    BIGINT       NULL,
    quantity   INT          NOT NULL,
    unit_price INT          NOT NULL,
    created_at DATETIME(6)  NOT NULL,
    PRIMARY KEY (id),
    KEY idx_order_item_order_id (order_id),
    KEY idx_order_item_menu_id (menu_id),
    KEY idx_order_item_bean_id (bean_id),
    CONSTRAINT fk_order_item_order FOREIGN KEY (order_id) REFERENCES orders (id),
    CONSTRAINT fk_order_item_menu  FOREIGN KEY (menu_id)  REFERENCES menu (id),
    CONSTRAINT fk_order_item_bean  FOREIGN KEY (bean_id)  REFERENCES bean (id)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;

CREATE TABLE review (
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    order_item_id BIGINT       NOT NULL,
    rating        TINYINT      NOT NULL,
    content       VARCHAR(500) NULL,
    created_at    DATETIME(6)  NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_review_order_item (order_item_id),
    CONSTRAINT fk_review_order_item FOREIGN KEY (order_item_id) REFERENCES order_item (id),
    CONSTRAINT chk_review_rating CHECK (rating BETWEEN 1 AND 5)
) ENGINE = InnoDB DEFAULT CHARSET = utf8mb4;
