package com.d9.seckill.controller;

import com.d9.seckill.dto.EventAdminDTO;
import com.d9.seckill.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final EventService eventService;

    @GetMapping("/events")
    @PreAuthorize("hasRole('ADMIN')")
    public List<EventAdminDTO> listAllEvents() {
        return eventService.getAllForAdmin();
    }
}
