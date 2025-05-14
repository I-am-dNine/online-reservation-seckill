package com.d9.seckill.controller;

import com.d9.seckill.dto.EventAdminDTO;
import com.d9.seckill.dto.ReservationAdminDTO;
import com.d9.seckill.service.EventService;
import com.d9.seckill.service.ReservationService;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final EventService eventService;
    private final ReservationService reservationService;

    @GetMapping("/events")
    @PreAuthorize("hasRole('ADMIN')")
    public List<EventAdminDTO> listAllEvents() {
        return eventService.getAllForAdmin();
    }

    @GetMapping("/reservations")
    @PreAuthorize("hasRole('ADMIN')")
    public List<ReservationAdminDTO> listReservations(@RequestParam(required = false) Long eventId) {
        return reservationService.getAll(Optional.ofNullable(eventId));
    }
}
