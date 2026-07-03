package com.brewtifulsip.bean.domain;

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
 * 원두 마스터 (ADR-0002). 상세 정보는 DB 단일 출처로 관리하고 GET /beans로 노출한다.
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "bean")
public class Bean extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(length = 100)
    private String origin;

    @Column(name = "roast_level", length = 50)
    private String roastLevel;

    @Column(length = 50)
    private String process;

    @Column(name = "flavor_notes", length = 255)
    private String flavorNotes;

    @Column(nullable = false)
    private boolean active;

    @Builder
    private Bean(String name, String origin, String roastLevel, String process,
                String flavorNotes, boolean active) {
        this.name = name;
        this.origin = origin;
        this.roastLevel = roastLevel;
        this.process = process;
        this.flavorNotes = flavorNotes;
        this.active = active;
    }
}
