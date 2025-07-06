package com.Shawarma.Shawarma.repository;

import com.Shawarma.Shawarma.model.AppUser;
import com.Shawarma.Shawarma.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByUser(AppUser user);
}
