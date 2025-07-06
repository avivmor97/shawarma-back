package com.Shawarma.Shawarma.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
public class Reservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String phoneNumber;

    private int guests;

    private LocalDateTime reservationTime;

    @ManyToOne
    @JoinColumn(name = "user_id") // עמודת foreign key שתקשר ל-AppUser
    private AppUser user;

    public Reservation() {}

    public Reservation(String username, String phoneNumber, int guests, LocalDateTime reservationTime) {
        this.username = username;
        this.phoneNumber = phoneNumber;
        this.guests = guests;
        this.reservationTime = reservationTime;
    }

    // Getters & Setters

    public Long getId() { return id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public int getGuests() { return guests; }
    public void setGuests(int guests) { this.guests = guests; }

    public LocalDateTime getReservationTime() { return reservationTime; }
    public void setReservationTime(LocalDateTime reservationTime) { this.reservationTime = reservationTime; }

    public AppUser getUser() { return user; }
    public void setUser(AppUser user) { this.user = user; }
}
