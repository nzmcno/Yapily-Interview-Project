# BankLite Project – Professional Implementation and Delivery Plan

## 🌟 Overview

**BankLite** is a lean, production-grade demo project that simulates an Open Banking aggregation platform. It is designed to mirror the architecture and core functionalities of real-world fintech API platforms like **Yapily**, offering CRUD-based account and transaction aggregation via REST APIs.

This project demonstrates end-to-end backend engineering capabilities using **Java 17**, **Spring Boot 3**, and **industry-standard security practices** (JWT, OAuth2). It is optimized for interviews, portfolio showcases, and foundational experience in building scalable financial backend services.

---

## 🎯 Core Objectives

- Simulate an Open Banking integration infrastructure used in companies like Yapily
- Develop a secure backend system with CRUD APIs for accounts and transactions
- Demonstrate API consumption from external banking sources using WebClient
- Showcase secure authentication and authorization using JWT and OAuth2
- Integrate input validation, rate limiting, test-driven development, and observability
- Build and package the application using Docker for portable deployment

---

## 🧩 Functional Modules

### 1. **Authentication & Authorization**

- JWT-based access tokens for secure API access
- Dummy OAuth2 token grant simulation via `POST /auth/token`
- Role-based access control using Spring Security (`@PreAuthorize`)

### 2. **Account Management**

- Full CRUD operations for account records
- `POST /accounts/sync` to simulate external bank data ingestion
- `GET /accounts/summary` to calculate the total balance from all linked accounts

### 3. **Transaction Management**

- CRUD and filter-enabled transaction management
- Query transactions by amount, date range, and account ID

### 4. **Bank API Integration Layer**

- WebClient-based mock HTTP integration clients
- WireMock used for realistic mock bank response simulations
- Retry and timeout logic to handle upstream availability issues

### 5. **Security and Governance**

- JWT authentication and authorization filters
- DTO-level input validation with `@Valid`, `@NotNull`, `@Size`
- Global rate limiter per token (memory-based, easily extendable)
- Secure headers, masking of sensitive logs
- CORS enabled for local development, CSRF disabled for APIs

### 6. **Health Monitoring and Observability**

- `/health/details` endpoint simulates external API health checks
- System-level Swagger docs for interactive API exploration
- Logging and basic fault-tolerance through structured handlers

### 7. **Testing and CI Readiness**

- JUnit 5 for unit testing
- WireMock for external API simulation
- Testcontainers (optional) for real DB testing
- Integration testing with Spring Boot Test and H2

---

## 📦 Tech Stack

| Layer       | Technology                                 |
| ----------- | ------------------------------------------ |
| Language    | Java 17                                    |
| Framework   | Spring Boot 3, Spring Data JPA             |
| HTTP Client | Spring WebClient                           |
| Database    | H2 (dev/test), PostgreSQL-ready setup      |
| Auth        | Spring Security (JWT, OAuth2 simulated)    |
| API Docs    | Springdoc OpenAPI / Swagger UI             |
| Build Tool  | Maven                                      |
| Packaging   | Dockerfile                                 |
| CI/CD       | GitHub Actions (build/test pipeline)       |
| Testing     | JUnit 5, Mockito, WireMock, Testcontainers |

---

## 🧱 Project Structure

```
src/
├── main/
│   ├── java/
│   │   └── com.banklite/
│   │       ├── auth/           # Authentication and JWT filters
│   │       ├── account/        # Account controllers and services
│   │       ├── transaction/    # Transaction logic and endpoints
│   │       ├── external/       # Mock bank API integration
│   │       ├── config/         # Security, Swagger, CORS settings
│   │       ├── dto/            # Request/response objects with validation
│   │       ├── entity/         # JPA entities
│   │       ├── repository/     # JPA repositories
│   │       └── service/        # Business logic implementations
│   └── resources/
│       └── application.yml     # App and DB config
└── test/
    ├── unit/                   # Unit tests with JUnit + Mockito
    └── integration/            # Integration tests with WireMock/H2
```

---

## 🧠 Engineering Best Practices

- **Clean Architecture**: Separation of concerns between Controller, Service, Repository, and DTO layers
- **Validation at Entry Point**: `@Valid` DTOs to prevent invalid data at controller level
- **Exception Handling**: Global error handler via `@ControllerAdvice` with custom error models
- **API Documentation**: Swagger/OpenAPI with live testing capability
- **Security First**: All endpoints (except token) are secured with JWT and token scope
- **Logging & Observability**: Structured logs with traceable request IDs
- **Code Coverage & Testing**: All business logic is unit tested, all external calls are mocked
- **Modular Scalability**: Easy to add new bank integrations or features via modular design

---

## 🔗 API Endpoints Overview

### 🔐 Authentication

- `POST /auth/token`

  - Request: `{ "clientId": "test", "clientSecret": "secret" }`
  - Response: `{ "access_token": "...", "expires_in": 3600 }`

### 📘 Accounts

- `POST /accounts`

  - Create a new account

- `GET /accounts/{id}`

  - Retrieve account details

- `PUT /accounts/{id}`

  - Update account info

- `DELETE /accounts/{id}`

  - Delete account

- `GET /accounts`

  - List all accounts

- `POST /accounts/sync`

  - Sync data from external mock bank

- `GET /accounts/summary`

  - Get total balance across accounts

### 💳 Transactions

- `POST /transactions`

  - Create new transaction

- `GET /transactions/{id}`

  - Retrieve a specific transaction

- `PUT /transactions/{id}`

  - Update a transaction

- `DELETE /transactions/{id}`

  - Remove a transaction

- `GET /transactions`

  - List transactions with filters:

    - `?accountId=...`
    - `?minAmount=...`
    - `?startDate=...&endDate=...`

### 🛠️ System

- `GET /health/details`

  - Returns health info of mock external bank integrations

- `GET /swagger-ui.html`

  - Interactive API documentation

All endpoints are secured by JWT (except `/auth/token`). Swagger UI supports token-based testing.

---

## 🗓️ 8-Day Development Timeline

| Day | Deliverables                             | Description                               |
| --- | ---------------------------------------- | ----------------------------------------- |
| 1   | Project skeleton, Swagger, Base config   | Initial structure and documentation setup |
| 2   | Entity + DTO modeling                    | Account, Transaction domain modeling      |
| 3   | CRUD endpoints for Account               | Implement basic operations and test them  |
| 4   | CRUD + filter endpoints for Transactions | Implement with pagination/filtering       |
| 5   | Mock API Integration via WebClient       | WireMock setup and data fetch simulation  |
| 6   | JWT Auth + Rate Limiting                 | Role-based access and protection setup    |
| 7   | Unit & Integration Testing               | Cover all endpoints and services          |
| 8   | Dockerfile + CI + README Finalization    | Prepare for deployment and presentation   |

---

## 🔒 Security Summary

- JWT token-based security (signed with HMAC)
- Custom `BearerTokenFilter` for request authorization
- Rate limiting: token-based limit per minute
- CORS: allowed only for `localhost`
- CSRF: disabled for REST API security best practices
- Logging filters: account numbers and tokens masked
- Validation: DTO fields checked at controller level

---

## 🧪 Testing Strategy

- `@SpringBootTest` + H2 for integration tests
- WireMock stubbing for mock bank APIs
- Mockito for service unit testing
- Auth and token behavior fully covered
- Swagger UI provides live manual testing coverage

---

## 🐳 Docker Instructions

```bash
mvn clean package
docker build -t banklite .
docker run -p 8080:8080 banklite
```

---

## 🎓 Sample Interview Statement

> "BankLite reflects my understanding of high-availability, testable Open Banking infrastructure. It features secure JWT auth, bank API simulation using WebClient + WireMock, DTO validation, rate limiting, and Swagger for self-documented APIs. It's structured using real-world backend layering, packaged via Docker, and ready for CI/CD pipelines."

---

## 📈 Optional Improvements (Post-Interview)

- OAuth2 login via Keycloak
- PostgreSQL + Flyway migrations
- Prometheus metrics + Grafana dashboard
- Kafka-based event publishing for transaction logs
- Admin user role with advanced access controls
- React-based frontend dashboard

---

## 👨‍💻 Author

**Nazimcan** – Software Developer, London-based
Passionate about fintech APIs, Open Banking, and scalable backend design

---

## 🧭 Purpose

> This project is built to showcase hands-on backend engineering capabilities in the fintech domain. It reflects real industry practices used by Open Banking infrastructure providers such as Yapily and others.

---
