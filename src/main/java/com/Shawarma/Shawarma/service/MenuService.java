package com.Shawarma.Shawarma.service;

import com.Shawarma.Shawarma.dto.MenuItemDTO;
import com.Shawarma.Shawarma.model.Ingredient;
import com.Shawarma.Shawarma.model.MenuItem;
import com.Shawarma.Shawarma.repository.IngredientRepository;
import com.Shawarma.Shawarma.repository.MenuItemRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MenuService {
    private final MenuItemRepository menuItemRepository;
    private final IngredientRepository ingredientRepository;

    public MenuService(MenuItemRepository menuItemRepository, IngredientRepository ingredientRepository) {
        this.menuItemRepository = menuItemRepository;
        this.ingredientRepository = ingredientRepository;
    }

    public List<MenuItem> getAllMenuItems() {
        return menuItemRepository.findAll();
    }

    public MenuItem addMenuItem(MenuItemDTO dto) {
        List<Ingredient> ingredients = ingredientRepository.findAllById(dto.ingredientIds);


        MenuItem item = new MenuItem();
        item.setName(dto.name);
        item.setDescription(dto.description);
        item.setPrice(dto.price);
        item.setImageUrl(dto.imageUrl);
        item.setIngredients(ingredients);

        return menuItemRepository.save(item);
    }

    public MenuItem getById(Long id) {
        return menuItemRepository.findById(id).orElse(null);
    }
}
