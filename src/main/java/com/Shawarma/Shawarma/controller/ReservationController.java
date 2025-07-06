package com.Shawarma.Shawarma.controller;

import com.Shawarma.Shawarma.dto.ReservationDTO;
import com.Shawarma.Shawarma.dto.ReservationRequest;
import com.Shawarma.Shawarma.model.AppUser;
import com.Shawarma.Shawarma.model.Reservation;
import com.Shawarma.Shawarma.repository.ReservationRepository;
import com.Shawarma.Shawarma.repository.UserRepository;
import com.Shawarma.Shawarma.service.ReservationService;
import com.Shawarma.Shawarma.service.SmsServiceClient;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.*;
import io.swagger.v3.oas.annotations.responses.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.List;

@RestController
@RequestMapping("/reservation")
@SecurityRequirement(name = "bearerAuth")
public class ReservationController {

    @Autowired
    private SmsServiceClient smsServiceClient;

    @Autowired
    private ReservationService reservationService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @Operation(
            summary = "Make a reservation",
            description = "Allows an authenticated user to make a reservation and receive SMS confirmation",
            requestBody = @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Reservation details",
                    required = true,
                    content = @Content(schema = @Schema(implementation = ReservationRequest.class))
            )
    )
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Reservation confirmed"),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "Unauthorized - JWT token missing or invalid")
    })
    @PostMapping
    public ResponseEntity<?> makeReservation(
            @RequestBody @Valid ReservationRequest request,
            Authentication authentication
    ) {
        String username = authentication.getName();

        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // תיקון הפרסינג לתאריך עם Z
        OffsetDateTime odt = OffsetDateTime.parse(request.getDateTime());
        LocalDateTime reservationTime = odt.toLocalDateTime();

        Reservation reservation = new Reservation();
        reservation.setUsername(username);
        reservation.setPhoneNumber(request.getPhoneNumber());
        reservation.setGuests(request.getGuests());
        reservation.setReservationTime(reservationTime);
        reservation.setUser(user);

        reservationService.save(reservation);

        smsServiceClient.sendReservationSms(
                request.getPhoneNumber(),
                username,
                request.getDateTime()
        );

        return ResponseEntity.ok("Reservation received and SMS sent.");
    }

    @GetMapping("/my")
    public List<ReservationDTO> getMyReservations(Authentication authentication) {
        String username = authentication.getName();
        AppUser user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Reservation> reservations = reservationRepository.findByUser(user);
        return reservations.stream()
                .map(ReservationDTO::fromEntity)
                .toList();
    }
}
