package com.d9.seckill.controller;

import com.d9.seckill.service.ReservationService;
import com.d9.seckill.dto.ReservationDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/reservation")
@RequiredArgsConstructor
@Tag(name = "预约管理", description = "用户预约相关接口")
public class ReservationController {

    private final ReservationService reservationService;

    @PostMapping("/{eventId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "创建预约", description = "用户预约指定活动")
    public String reserve(@PathVariable Long eventId) {
        return reservationService.reserve(eventId);
    }

    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "我的预约", description = "查看当前用户的所有预约")
    public List<ReservationDTO> myReservations() {
        return reservationService.getMyReservations();
    }
}
