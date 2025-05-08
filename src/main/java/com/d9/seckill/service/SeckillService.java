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
        "-- 检查库存是否存在\n" +
        "local stock = redis.call('get', KEYS[1])\n" +
        "if stock == false then\n" +  // 明确检查 false (nil)
        "  return 0\n" + // 无库存值
        "end\n" +
        "-- 转换库存为数字\n" +
        "local stockNum = tonumber(stock)\n" +
        "if stockNum == nil then\n" +  // 检查转换失败
        "  return -1\n" + // 库存值不是有效数字
        "end\n" +
        "-- 检查库存是否足够\n" +
        "if stockNum > 0 then\n" +
        "  -- 检查用户是否已抢购\n" +
        "  if redis.call('setnx', KEYS[2], ARGV[1]) == 1 then\n" +
        "    -- 设置防重键过期时间，直接使用 ARGV[2]，不做 tonumber 转换\n" +
        "    redis.call('expire', KEYS[2], ARGV[2])\n" +
        "    -- 减少库存\n" +
        "    redis.call('decrby', KEYS[1], 1)\n" +  // 使用 decrby 代替 decr
        "    return 1\n" + // 抢购成功
        "  else\n" +
        "    return 2\n" + // 重复请求
        "  end\n" +
        "else\n" +
        "  return 0\n" + // 库存不足
        "end";

    public String doSeckill(String username, Long eventId) {
        String stockKey = "event:stock:" + eventId;
        String userKey = "seckill:user:" + eventId + ":" + username;

        DefaultRedisScript<Long> redisScript = new DefaultRedisScript<>();
        redisScript.setScriptText(LUA_SCRIPT);
        redisScript.setResultType(Long.class);

        try {
            // 确保 Redis 中有库存值
            Object stockValue = redisTemplate.opsForValue().get(stockKey);
            if (stockValue == null) {
                System.out.println("库存键 " + stockKey + " 不存在，正在初始化...");
                // 这里可以添加初始化库存的逻辑，如果需要的话
            } else {
                System.out.println("当前库存值: " + stockValue + ", 类型: " + stockValue.getClass().getName());
            }
            
            Long result = redisTemplate.execute(
                    redisScript,
                    Arrays.asList(stockKey, userKey),
                    username, 300 // 防重 key 保留 5 分钟 - 使用整数而不是字符串
            );
            
            System.out.println("Lua 脚本执行结果: " + result);
            
            if (result == 1L) {
                // 成功，发送到 MQ
                SeckillMessage message = new SeckillMessage(username, eventId);
                rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE, RabbitMQConfig.ROUTING_KEY, message);
                return "抢购成功，排队处理中";
            } else if (result == 2L) {
                return "你已抢购过该项目";
            } else if (result == -1L) {
                System.err.println("Redis 库存值不是有效数字");
                return "系统错误，库存数据异常";
            } else {
                return "抢购失败，库存不足";
            }
        } catch (Exception e) {
            // 详细记录错误
            System.err.println("Redis operation failed: " + e.getMessage());
            e.printStackTrace(); // 打印完整堆栈跟踪
            return "系统繁忙，请稍后重试";
        }
    }
}
