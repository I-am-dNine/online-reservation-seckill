package com.d9.seckill.controller;

import com.d9.seckill.service.ReservationService;
import com.d9.seckill.dto.ReservationDTO;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservation")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/{eventId}")
    @PreAuthorize("hasRole('USER')")
    public String reserve(@PathVariable Long eventId) {
        return reservationService.reserve(eventId);
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public List<ReservationDTO> myReservations() {
        return reservationService.getMyReservations();
    }
}
