package com.brewtifulsip.menu.service;

import com.brewtifulsip.menu.dto.MenuResponse;
import com.brewtifulsip.menu.repository.MenuRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class MenuService {

    private final MenuRepository menuRepository;

    public MenuService(MenuRepository menuRepository) {
        this.menuRepository = menuRepository;
    }

    public List<MenuResponse> getActiveMenus() {
        return menuRepository.findByActiveTrueOrderByIdAsc().stream()
                .map(MenuResponse::from)
                .toList();
    }
}
