package com.Shawarma.Shawarma.service;

import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class SmsServiceClient {

    private final RestTemplate restTemplate = new RestTemplate();

    public void sendLoginSms(String phoneNumber, String username) {
        String smsUrl = "http://localhost:8081/sms/send-login";

        Map<String, String> requestBody = Map.of(
                "phoneNumber", phoneNumber,
                "username", username
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

        try {
            restTemplate.postForEntity(smsUrl, request, String.class);
        } catch (Exception e) {
            System.err.println("❌ Failed to send login SMS: " + e.getMessage());
        }
    }

    public void sendCustomSms(String phoneNumber, String message) {
        String smsUrl = "http://localhost:8081/sms/send";

        Map<String, String> requestBody = Map.of(
                "phoneNumber", phoneNumber,
                "message", message
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

        try {
            restTemplate.postForEntity(smsUrl, request, String.class);
        } catch (Exception e) {
            System.err.println("❌ Failed to send custom SMS: " + e.getMessage());
        }
    }

    // ✅ גרסה מעודכנת ששולחת גם תאריך
    public void sendReservationSms(String phoneNumber, String username, String reservationDateTime) {
        String smsUrl = "http://localhost:8081/sms/send-reservation";

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("phoneNumber", phoneNumber);
        requestBody.put("username", username);
        requestBody.put("reservationDateTime", reservationDateTime); // ✅ תאריך ההזמנה

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);

        try {
            restTemplate.postForEntity(smsUrl, request, String.class);
        } catch (Exception e) {
            System.err.println("❌ Failed to send reservation SMS: " + e.getMessage());
        }
    }
}
