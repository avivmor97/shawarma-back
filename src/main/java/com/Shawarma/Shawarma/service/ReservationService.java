package com.Shawarma.Shawarma.service;

import com.Shawarma.Shawarma.model.Reservation;
import com.Shawarma.Shawarma.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    public Reservation save(Reservation reservation) {
        return reservationRepository.save(reservation);
    }
}
