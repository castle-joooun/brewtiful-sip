package com.brewtifulsip.menu.dto;

import com.brewtifulsip.menu.domain.Menu;

public record MenuResponse(Long id, String name, int price, boolean requiresBean) {

    public static MenuResponse from(Menu menu) {
        return new MenuResponse(menu.getId(), menu.getName(), menu.getPrice(), menu.isRequiresBean());
    }
}
