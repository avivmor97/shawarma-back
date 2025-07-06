package com.Shawarma.Shawarma.repository;

import com.Shawarma.Shawarma.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // future filtering by user, date, etc.
}
