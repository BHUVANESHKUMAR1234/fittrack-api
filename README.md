# FitTrack API

> Spring Boot 3.x REST API for the FitTrack Fitness Tracker — JWT auth, workout logging, nutrition tracking, AI workout plans, and progress analytics.

[![CI Pipeline](https://github.com/BHUVANESHKUMAR1234/fittrack-api/actions/workflows/ci.yml/badge.svg)](https://github.com/BHUVANESHKUMAR1234/fittrack-api/actions)
[![Java](https://img.shields.io/badge/Java-21-blue)](https://adoptium.net/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3-green)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue)](https://www.postgresql.org/)

---

## Tech Stack

| Layer | Technology |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.3 |
| Security | Spring Security + JWT (JJWT) |
| Database | PostgreSQL 16 |
| Migrations | Flyway |
| ORM | Spring Data JPA / Hibernate |
| Mapping | MapStruct |
| Boilerplate | Lombok |
| API Docs | SpringDoc OpenAPI 3 (Swagger UI) |
| Testing | JUnit 5 + Mockito + Testcontainers |
| Coverage | JaCoCo (80% minimum enforced) |
| Monitoring | Actuator + Micrometer + Prometheus |
| Containerisation | Docker (multi-stage) |
| Orchestration | Kubernetes + Helm |
| CI/CD | GitHub Actions |

---

## Project Structure

```
src/main/java/com/fittrack/
├── config/         # Security, CORS, JWT properties, OpenAPI
├── controller/     # REST controllers (thin — no business logic)
├── service/        # Business logic layer
├── repository/     # JPA repositories
├── domain/
│   ├── entity/     # JPA entities (User, WorkoutSession, ExerciseLog, NutritionLog)
│   └── enums/      # Role, WorkoutType, MealType
├── dto/
│   ├── request/    # Validated request DTOs
│   └── response/   # Response DTOs
├── mapper/         # MapStruct mappers
├── exception/      # GlobalExceptionHandler + custom exceptions
├── security/       # JwtTokenProvider, JwtAuthenticationFilter
└── util/           # Constants, helpers
```

---

## Getting Started (Local)

### Prerequisites
- Java 21+
- Docker & Docker Compose
- Maven 3.9+

### Run with Docker Compose

```bash
git clone https://github.com/BHUVANESHKUMAR1234/fittrack-api.git
cd fittrack-api
docker-compose up -d
```

API available at: `http://localhost:8080/api/v1`
Swagger UI: `http://localhost:8080/api/v1/swagger-ui.html`

### Run locally (without Docker)

```bash
# Start PostgreSQL only
docker-compose up -d postgres

# Run the app
SPRING_PROFILES_ACTIVE=dev mvn spring-boot:run
```

---

## API Endpoints

| Method | Endpoint | Description | Auth |
|---|---|---|---|
| POST | `/auth/register` | Register new user | Public |
| POST | `/auth/login` | Login, get JWT | Public |
| POST | `/auth/refresh` | Refresh access token | Public |
| GET | `/users/me` | Get current user profile | JWT |
| PUT | `/users/me` | Update profile | JWT |
| GET | `/workouts` | List user's workouts | JWT |
| POST | `/workouts` | Log a new workout | JWT |
| GET | `/workouts/{id}` | Get workout detail | JWT |
| PUT | `/workouts/{id}` | Update workout | JWT |
| DELETE | `/workouts/{id}` | Delete workout | JWT |
| GET | `/nutrition` | List nutrition logs | JWT |
| POST | `/nutrition` | Add nutrition entry | JWT |
| GET | `/progress/summary` | Get progress stats | JWT |
| GET | `/progress/streak` | Get current streak | JWT |

---

## Running Tests

```bash
# Unit + integration tests with coverage report
mvn clean verify

# View coverage report
open target/site/jacoco/index.html
```

Coverage minimum: **80% line coverage** (enforced by JaCoCo in CI).

---

## Environment Variables

| Variable | Default | Description |
|---|---|---|
| `DB_URL` | `jdbc:postgresql://localhost:5432/fittrack` | PostgreSQL URL |
| `DB_USERNAME` | `fittrack` | DB username |
| `DB_PASSWORD` | `fittrack` | DB password |
| `JWT_SECRET` | *(see config)* | Base64 encoded secret — **change in prod** |
| `JWT_EXPIRATION_MS` | `86400000` | Access token TTL (24h) |
| `SERVER_PORT` | `8080` | Server port |

---

## Related Repositories

- [`fittrack-web`](https://github.com/BHUVANESHKUMAR1234/fittrack-web) — React 18 + TypeScript frontend
- [`fittrack-infra`](https://github.com/BHUVANESHKUMAR1234/fittrack-infra) — Docker, Helm, Kubernetes, CI/CD

---

## Author

**Bhuvanesh Kumar** — Full Stack Developer
