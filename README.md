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

## ✨ Author

Powered by D9
