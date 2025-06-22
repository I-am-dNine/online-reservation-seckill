# 🚀 Deployment Guide

This guide explains how to deploy the Seckill Reservation System in different environments.

---

## 💻 Option 1: Docker Compose (Recommended)

```bash
docker-compose up --build
```
Access the system at:
- Frontend: http://localhost:8080
- Swagger: http://localhost:8080/swagger-ui.html

---

## 💻 Option 2: Local Development Setup

Prerequisites:
- Java 17+
- Maven 3.8+
- MySQL (with schema seckill)
- Redis & RabbitMQ

Steps:
1. Setup local DB:
```sql
CREATE DATABASE seckill;
```
2. Configure application-dev.yml with your DB settings.
3. Run:
```zsh
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

---

## 💻 Option 3: Run with H2 Test Profile

```zsh
./mvnw test -Dspring.profiles.active=test
```
This uses:
- H2 in-memory DB
- Mockito / Testcontainers (if enabled)
