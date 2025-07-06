package com.Shawarma.Shawarma.dto;

public class ReservationRequest {
    private String phoneNumber;
    private int guests;
    private String dateTime; // ✅ זה תואם לפרונט – אל תשנה!

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public int getGuests() { return guests; }
    public void setGuests(int guests) { this.guests = guests; }

    public String getDateTime() { return dateTime; }
    public void setDateTime(String dateTime) { this.dateTime = dateTime; }
}
