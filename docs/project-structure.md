# 🧱 Project Structure Overview

A quick overview of how the project is structured.

---

## 📆 Module Breakdown

| Module/Folder     | Description |
|-------------------|-------------|
| `auth/`           | Handles user registration, login, JWT authentication |
| `event/`          | CRUD logic for event management |
| `reservation/`    | Main reservation logic, including availability check, rate limit, etc. |
| `admin/`          | Admin backend interfaces |
| `config/`         | Security, CORS, exception handler, etc. |
| `docs/`           | Markdown documentation |
| `test/`           | Unit + integration tests |
| `resources/`      | YAML config files, templates, static assets |
| `Dockerfile`      | Application containerization |
| `docker-compose.yml` | Compose Redis, RabbitMQ, MySQL |

---

## 🌐 Controller Endpoints

- `/api/auth/**` – login & JWT
- `/api/event/**` – CRUD for events
- `/api/reservation/**` – reservation logic
- `/api/admin/**` – admin viewing