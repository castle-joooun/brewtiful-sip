package com.brewtifulsip.bean.dto;

import com.brewtifulsip.bean.domain.Bean;

/**
 * 원두 조회 응답 DTO. Entity 직접 노출 금지(CLAUDE.md 4장). openapi.yaml BeanResponse 대응.
 */
public record BeanResponse(
        Long id,
        String name,
        String origin,
        String roastLevel,
        String process,
        String flavorNotes
) {
    public static BeanResponse from(Bean bean) {
        return new BeanResponse(
                bean.getId(),
                bean.getName(),
                bean.getOrigin(),
                bean.getRoastLevel(),
                bean.getProcess(),
                bean.getFlavorNotes()
        );
    }
}
