package com.d9.seckill.controller;

import com.d9.seckill.dto.SeckillMessage;
import com.d9.seckill.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.amqp.rabbit.core.RabbitTemplate;

@RestController
@RequestMapping("/api/seckill")
@RequiredArgsConstructor
public class SeckillController {

    private final RedisTemplate<String, Object> redisTemplate;

    private final RabbitTemplate rabbitTemplate;

    @PostMapping("/{eventId}")
    @PreAuthorize("hasRole('USER')")
    public String seckill(@PathVariable Long eventId) {
        String key = "event:stock:" + eventId;
        Long stock = redisTemplate.opsForValue().decrement(key);

        if (stock == null || stock < 0) {
            return "抢购失败，名额已满";
        }

        String username = SecurityContextHolder.getContext().getAuthentication().getName();

        SeckillMessage msg = new SeckillMessage(username, eventId);
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, msg);

        return "抢购成功，已送入排队处理";
    }

}