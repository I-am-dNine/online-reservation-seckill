package com.d9.seckill.controller;

import com.d9.seckill.service.SeckillService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/seckill")
@RequiredArgsConstructor
@Tag(name = "秒杀管理", description = "秒杀活动相关接口")
public class SeckillController {

    private final SeckillService seckillService;

    @PostMapping("/{eventId}")
    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "参与秒杀", description = "用户参与指定活动的秒杀")
    public String seckill(@PathVariable Long eventId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return seckillService.doSeckill(username, eventId);
    }

}