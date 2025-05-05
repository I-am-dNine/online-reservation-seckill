package com.d9.seckill.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/seckill")
@RequiredArgsConstructor
public class SeckillController {

    private final RedisTemplate<String, Object> redisTemplate;

    @PostMapping("/{eventId}")
    @PreAuthorize("hasRole('USER')")
    public String seckill(@PathVariable Long eventId) {
        String key = "event:stock:" + eventId;

        // Redis 扣库存
        Long stock = redisTemplate.opsForValue().decrement(key);

        if (stock == null || stock < 0) {
            return "抢购失败，名额已满";
        }

        // TODO: 后续可将请求送入 MQ，此处暂时回传成功
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return "抢购成功！" + username + " 排队处理中（库存剩余：" + stock + "）";
    }
}