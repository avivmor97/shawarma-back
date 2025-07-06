package com.Shawarma.Shawarma.controller;

import com.Shawarma.Shawarma.dto.MenuItemDTO;
import com.Shawarma.Shawarma.model.MenuItem;
import com.Shawarma.Shawarma.service.MenuService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menu")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping
    public List<MenuItem> getAllMenuItems() {
        return menuService.getAllMenuItems();
    }

    @PostMapping
    public MenuItem addMenuItem(@RequestBody MenuItemDTO dto) {
        return menuService.addMenuItem(dto);
    }

    @GetMapping("/{id}")
    public MenuItem getItem(@PathVariable Long id) {
        return menuService.getById(id);
    }
}
