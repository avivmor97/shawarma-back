package com.Shawarma.Shawarma.repository;

import com.Shawarma.Shawarma.model.MenuItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
}
