package com.Shawarma.Shawarma.config;

import com.Shawarma.Shawarma.model.MenuItem;
import com.Shawarma.Shawarma.repository.MenuItemRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class DataSeeder {

    private final MenuItemRepository menuItemRepository;

    public DataSeeder(MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    @PostConstruct
    public void seedData() {
        if (menuItemRepository.count() == 0) {
            menuItemRepository.save(new MenuItem(null, "Shawarma in Pita", "", 32.90, "", Collections.emptyList()));
            menuItemRepository.save(new MenuItem(null, "Shawarma in Lafa", "", 39.90, "", Collections.emptyList()));
            menuItemRepository.save(new MenuItem(null, "Shawarma Plate", "", 42.90, "", Collections.emptyList()));
            menuItemRepository.save(new MenuItem(null, "Hummus", "", 9.90, "", Collections.emptyList()));
            menuItemRepository.save(new MenuItem(null, "French Fries", "", 10.90, "", Collections.emptyList()));
            menuItemRepository.save(new MenuItem(null, "Green Salad", "", 11.90, "", Collections.emptyList()));
            menuItemRepository.save(new MenuItem(null, "Cola", "", 7.00, "", Collections.emptyList()));
            menuItemRepository.save(new MenuItem(null, "Cola Zero", "", 7.00, "", Collections.emptyList()));
        }
    }
}
