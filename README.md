# Seckill Reservation System

A high-concurrency online reservation & seckill system built with Spring Boot, MySQL, Redis, RabbitMQ, and other modern backend technologies.

## 🔧 Tech Stack

- Java 17
- Spring Boot 3
- Spring Data JPA
- Spring Security + JWT
- MySQL
- Redis (for caching & locking)
- RabbitMQ (for async processing)
- Docker (for container deployment)

## 📌 Project Stages

1. **Environment Setup & Base Functions**
   - Spring Boot skeleton project
   - MySQL + JPA configuration
   - User registration/login (JWT auth)
   - Basic Event CRUD API

2. **Low Concurrency Reservation Flow**
   - Prevent duplicate booking
   - Basic error handling

3. **High Concurrency Optimization**
   - Optimistic Lock
   - Redis stock caching
   - Rate limiting
   - RabbitMQ async queue

4. **Finalization & Polish**
   - Admin interface
   - Logging, Docker, Swagger docs
   - Frontend (simple React or Thymeleaf)

## ❓Q&A

### Q1：如果一百万人同时抢票，系统怎么决定谁先谁后？

**A1：**  
虽然用户看起来是“同时”点击抢购按钮，但实际上，从请求发出到被系统处理，会经过多个阶段，每一层都存在**细微但真实的时间差**：

> 🕸️ 网络传输 → 🖥️ 服务器接收 → ⚙️ 系统调度 → 📦 Redis 排队执行

Redis 是**单线程运行**的，因此会**严格按照请求抵达 Redis 的顺序（FIFO）**依次处理。谁先到，谁就先被处理，这也就自然决定了谁能抢到票。

---

### 📌 补充：Redis 如何保持顺序？

✅ Redis 是单线程执行模型，这意味着它在任意时刻**只处理一个请求**。  
即使两个请求仅相差 0.000001 秒，它也会：

🔧 如何记录“谁先抢到”？
可以借助 Redis 的 ZADD（有序集合）命令，将用户 ID 和当前时间戳作为分值写入排行榜，从而实现“谁先抢到”的精确排名逻辑。例如：

```redis
ZADD ticket_rankings <timestamp> <user_id>
```
这样你就可以快速获取前 10 名、前 100 名等信息：
```redis
ZRANGE ticket_rankings 0 9 WITHSCORES
```

🔚 总结
所谓“同时抢票”其实是一个近似的假象。系统的核心抢购逻辑本质上依赖两个机制：
- 系统各层级的天然时间差异（网络传输、调度等）
- Redis 单线程 + 排队机制确保请求有序处理
这就是为什么即使上百万人一起抢，也始终会有一个明确的“第一名”。


## ✨ Author
Powered by D9
