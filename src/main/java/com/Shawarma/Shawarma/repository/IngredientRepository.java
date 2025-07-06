package com.Shawarma.Shawarma.repository;

import com.Shawarma.Shawarma.model.Ingredient;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IngredientRepository extends JpaRepository<Ingredient, Long> {


}
