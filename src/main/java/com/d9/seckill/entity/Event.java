package com.d9.seckill.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "events")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(length = 500)
    private String description;

    @Column(nullable = false)
    private Integer totalSlots; // 总名额

    @Column(nullable = false)
    private Integer availableSlots; // 剩余名额

    private LocalDateTime startTime;

    private LocalDateTime endTime;
}