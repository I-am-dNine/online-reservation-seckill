package com.d9.seckill.controller;

import com.d9.seckill.service.SeckillService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/seckill")
@RequiredArgsConstructor
public class SeckillController {

    private final SeckillService seckillService;

    @PostMapping("/{eventId}")
    @PreAuthorize("hasRole('USER')")
    public String seckill(@PathVariable Long eventId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return seckillService.doSeckill(username, eventId);
    }

}