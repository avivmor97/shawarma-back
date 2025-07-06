package com.Shawarma.Shawarma.dto;

import com.Shawarma.Shawarma.model.Reservation;

public class ReservationDTO {
    private String dateTime;
    private int guests;

    public static ReservationDTO fromEntity(Reservation reservation) {
        ReservationDTO dto = new ReservationDTO();
        dto.setDateTime(reservation.getReservationTime().toString());
        dto.setGuests(reservation.getGuests());
        return dto;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getGuests() {
        return guests;
    }

    public void setGuests(int guests) {
        this.guests = guests;
    }
}
