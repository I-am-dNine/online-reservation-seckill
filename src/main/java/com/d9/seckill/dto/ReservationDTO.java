package com.d9.seckill.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class ReservationDTO {
    private String eventTitle;
    private LocalDateTime reservedAt;
    private LocalDateTime eventStartTime;
    private LocalDateTime eventEndTime;
}