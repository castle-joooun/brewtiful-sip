package com.brewtifulsip.menu.api;

import com.brewtifulsip.menu.dto.MenuResponse;
import com.brewtifulsip.menu.service.MenuService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/menus")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping
    public List<MenuResponse> listMenus() {
        return menuService.getActiveMenus();
    }
}
