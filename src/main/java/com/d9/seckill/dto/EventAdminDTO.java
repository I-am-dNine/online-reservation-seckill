package com.d9.seckill.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class EventAdminDTO {
    private Long id;
    private String title;
    private Integer availableSlots;
    private Integer totalSlots;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
}

