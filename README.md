# ⚡ Seckill Reservation System

A **high-concurrency online reservation & flash sale system** built with Java, Spring Boot, Redis, RabbitMQ and Docker.  
Designed to **handle massive concurrent requests** with reliability, responsiveness, and fairness.

> 🎯 Project Goal: Build a scalable system for high-volume reservation/seckill events, with full-stack features and real-world architecture patterns.

---

## 🪜 Project Stages

1. **Environment Setup & Base Functions**
   - ✅ Spring Boot project skeleton
   - ✅ MySQL + JPA integration
   - ✅ User registration & JWT login
   - ✅ Event CRUD API

2. **Low Concurrency Reservation Flow**
   - ✅ Reservation core logic
   - ✅ Duplicate reservation prevention
   - ✅ Basic error handling

3. **High Concurrency Optimization**
   - ✅ Redis cache & stock deduction (Lua script)
   - ✅ Optimistic locking for race condition safety
   - ✅ Rate limiting (Guava / Redis)
   - ✅ RabbitMQ async queue processing

4. **System Enhancement & Polish**
   - ✅ Admin interface for event & reservation record
   - ✅ Global exception handling
   - ✅ Swagger3 API documentation
   - ✅ Docker deployment
   - ✅ Thymeleaf frontend (React optional)

---

## 🔥 Key Features

- ✅ User registration & login with JWT
- ✅ Create and manage events (CRUD)
- ✅ Reservation API with duplicate prevention
- ✅ Redis cache & Lua script for atomic stock deduction
- ✅ Rate limiting (Guava / Redis)
- ✅ RabbitMQ async queue for traffic peak smoothing
- ✅ Admin interface for viewing reservation records
- ✅ Global exception handler & logging
- ✅ Swagger API docs
- ✅ Docker containerization
- ✅ Frontend interface (Thymeleaf version ready)

---

## 🧱 Tech Stack

| Layer      | Technology |
|------------|------------|
| Language   | Java 17 |
| Backend    | Spring Boot 3, Spring Security, JWT |
| Database   | MySQL + Spring Data JPA |
| Cache      | Redis |
| Messaging  | RabbitMQ |
| Frontend   | Thymeleaf (React optional) |
| Tools      | Swagger 3 / OpenAPI, Docker, JUnit 5 |

---

## 🚀 Quick Start

### 🐳 Option 1: Run via Docker Compose

```bash
docker-compose up --build
```
Then visit:
- Frontend UI: http://localhost:8080
- Swagger API Docs: http://localhost:8080/swagger-ui.html

✨ Default Admin Account: admin / admin123

### 💻 Option 2: Local Run (Dev Profile)
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```
Requires:
- Java 17+
- MySQL running with schema seckill
- Redis and RabbitMQ running locally

---

## 📄 Project Modules

- auth - JWT login/logout logic
- event - Activity/event CRUD
- reservation - Reservation service & controller
- admin - Backend management page
- config - Security + global exception + test profiles

---

## 📚 Documentation

| Topic                    | File                                                 |
| ------------------------ | ---------------------------------------------------- |
| 🔐 Auth Flow             | [`docs/auth-flow.md`](docs/auth-flow.md)             |
| 🧱 System Architecture   | [`docs/system-overview.md`](docs/system-overview.md) |
| 🗃️ DB Schema Design      | [`docs/db-design.md`](docs/db-design.md)             |
| ⚙️ High Concurrency Flow | [`docs/seckill-flow.md`](docs/seckill-flow.md)       |

---

## ✅ Test Coverage

- UserServiceTest ✅
- EventServiceTest ✅
- ReservationServiceTest ✅
- ReservationControllerTest ✅

Use:
```bash
./mvnw test
```

---

## 👨‍💻 Author

Created by D9

---

## ⭐ Future Enhancements (Ideas)

-[] Add React SPA frontend (optional)
-[] Add dynamic feature flags (per event)
-[] Add metrics dashboard (Actuator / Prometheus)