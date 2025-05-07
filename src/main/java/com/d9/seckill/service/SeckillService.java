package com.d9.seckill.service;

import com.d9.seckill.config.RabbitMQConfig;
import com.d9.seckill.dto.SeckillMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
@RequiredArgsConstructor
public class SeckillService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RabbitTemplate rabbitTemplate;

    // -- KEYS[1] = 库存 key
    // -- KEYS[2] = 用户防重 key
    // -- ARGV[1] = 用户名
    // -- ARGV[2] = 防重 key 过期时间（秒）
    private static final String LUA_SCRIPT =
            "if redis.call('get', KEYS[1]) and tonumber(redis.call('get', KEYS[1])) > 0 then\n" +
            "  if redis.call('setnx', KEYS[2], ARGV[1]) == 1 then\n" +
            "    redis.call('expire', KEYS[2], ARGV[2])\n" +
            "    redis.call('decr', KEYS[1])\n" +
            "    return 1\n" +
            "  else\n" +
            "    return 2\n" + // -- 重复请求
            "  end\n" +
            "else\n" +
            "  return 0\n" + // -- 库存不足
            "end";

    public String doSeckill(String username, Long eventId) {
        String stockKey = "event:stock:" + eventId;
        String userKey = "seckill:user:" + eventId + ":" + username;

        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(LUA_SCRIPT);
        redisScript.setResultType(Long.class);

        Long result = redisTemplate.execute(
                redisScript,
                Arrays.asList(stockKey, userKey),
                username, "300" // 防重 key 保留 5 分钟
        );

        if (result == 1L) {
            // 成功，发送到 MQ
            SeckillMessage message = new SeckillMessage(username, eventId);
            rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, message);
            return "抢购成功，排队处理中";
        } else if (result == 2L) {
            return "你已抢购过该项目";
        } else {
            return "抢购失败，库存不足";
        }
    }
}