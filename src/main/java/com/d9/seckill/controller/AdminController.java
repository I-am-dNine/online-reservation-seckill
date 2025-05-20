package com.d9.seckill.controller;

import com.d9.seckill.dto.EventAdminDTO;
import com.d9.seckill.dto.PagedResult;
import com.d9.seckill.dto.ReservationAdminDTO;
import com.d9.seckill.service.EventService;
import com.d9.seckill.service.ReservationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@Tag(name = "管理员接口", description = "管理员专用接口，包含活动和预约管理")
public class AdminController {

    private final EventService eventService;
    private final ReservationService reservationService;

    @GetMapping("/events")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "获取所有活动", description = "管理员查看所有活动列表")
    public List<EventAdminDTO> listAllEvents() {
        return eventService.getAllForAdmin();
    }

    @GetMapping("/reservations")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "获取预约列表", description = "管理员查看预约列表，支持分页和按活动筛选")
    public PagedResult<ReservationAdminDTO> listReservations(
        @RequestParam Optional<Long> eventId,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        return reservationService.getAllPaged(eventId, page, size);
    }
}
