package com.d9.seckill.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ReservationAdminDTO {
    private Long reservationId;
    private String username;
    private String eventTitle;
    private LocalDateTime reservedAt;
}
