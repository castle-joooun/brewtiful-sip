package com.brewtifulsip.menu.domain;

import com.brewtifulsip.common.entity.BaseTimeEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 판매 메뉴. requiresBean=true 인 메뉴는 주문 시 원두 선택이 필요하다.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "menu")
public class Menu extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false)
    private int price;

    @Column(name = "requires_bean", nullable = false)
    private boolean requiresBean;

    @Column(nullable = false)
    private boolean active;

    @Builder
    private Menu(String name, int price, boolean requiresBean, boolean active) {
        this.name = name;
        this.price = price;
        this.requiresBean = requiresBean;
        this.active = active;
    }
}
