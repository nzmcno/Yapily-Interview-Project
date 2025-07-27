# BankLite - Interview Project

> **A production-ready banking REST API built for Yapily interview process**

## Overview

BankLite is a lightweight, containerized banking account management system demonstrating modern Spring Boot 3 development practices. This project showcases essential CRUD operations, comprehensive testing, API documentation, and professional deployment strategies - all implemented as part of the technical interview process for Yapily.

**Project Purpose**: To demonstrate proficiency in Java enterprise development, RESTful API design, database integration, and DevOps practices within the context of Open Banking and financial technology.

## 🏗️ Technical Architecture

### Core Technologies
- **Java 21** with Spring Boot 3.x
- **Spring Data JPA** for data persistence
- **PostgreSQL 15** for production database
- **H2** for development and testing
- **Docker & Docker Compose** for containerization
- **Maven** for dependency management
- **OpenAPI/Swagger** for comprehensive API documentation

### Key Features
- ✅ **RESTful API Design** - Complete CRUD operations with proper HTTP status codes
- ✅ **Multi-Environment Support** - Development (H2) and Production (PostgreSQL) profiles
- ✅ **Comprehensive Testing** - Unit tests, integration tests, and API validation
- ✅ **Production-Ready** - Health checks, monitoring, and error handling
- ✅ **Docker Deployment** - Complete containerization with database setup
- ✅ **API Documentation** - Interactive Swagger UI for API exploration

## 🚀 Quick Start

### Prerequisites
- Java 21+
- Maven 3.8+
- Docker & Docker Compose
- Git

### 1. Clone and Build
```bash
git clone <repository-url>
cd Yapily-Interview-Project
cd Banklite
mvn clean package -DskipTests
```

### 2. Run with Docker (Recommended)
```bash
# Start all services with PostgreSQL
docker-compose up --build -d

# Verify deployment
curl http://localhost:8080/actuator/health
```

### 3. Alternative: Local Development
```bash
# Run with H2 in-memory database
mvn spring-boot:run

# Or with specific profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

## 📋 API Endpoints

### Base URLs
- **Local Development**: `http://localhost:8080`
- **Docker Deployment**: `http://localhost:8080`

### Account Management
| Method | Endpoint | Description | Response |
|--------|----------|-------------|----------|
| `POST` | `/api/v1/accounts` | Create new account | `201 Created` |
| `GET` | `/api/v1/accounts` | Get all accounts | `200 OK` |
| `GET` | `/api/v1/accounts/{id}` | Get account by ID | `200 OK` |
| `PUT` | `/api/v1/accounts/{id}` | Update account | `200 OK` |
| `DELETE` | `/api/v1/accounts/{id}` | Delete account | `204 No Content` |

### System Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/actuator/health` | System health check |
| `GET` | `/swagger-ui.html` | Interactive API documentation |

## 💾 Database Design

### Account Entity Schema
```sql
CREATE TABLE accounts (
    id BIGSERIAL PRIMARY KEY,
    account_holder_name VARCHAR(255) NOT NULL,
    account_number VARCHAR(50) NOT NULL UNIQUE,
    balance DECIMAL(19,2) NOT NULL DEFAULT 0.00,
    currency VARCHAR(3) NOT NULL CHECK (currency IN ('USD', 'EUR', 'GBP')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
```

### Sample Data
The system comes pre-populated with demonstration accounts:
- John Doe - $1,500.00 USD
- Jane Smith - €2,750.50 EUR  
- Bob Johnson - £850.25 GBP
- Alice Brown - $3,200.00 USD
- Charlie Wilson - €975.75 EUR

## 🧪 Testing Strategy

### Test Coverage
```
Tests: 13 total, 100% pass rate
├── AccountServiceTest: 7 unit tests (Service layer)
├── AccountControllerIntegrationTest: 5 integration tests (API layer)
└── BankliteApplicationTests: 1 application context test
```

### Testing Approach
- **Unit Testing**: Mockito for service layer isolation
- **Integration Testing**: TestRestTemplate for full API validation
- **Test Database**: H2 in-memory for clean test isolation
- **Test-Driven Development**: Comprehensive test coverage before implementation

## 🐳 Containerization

### Docker Architecture
```yaml
services:
  postgres:     # PostgreSQL 15 with health checks
  banklite-app: # Spring Boot application with monitoring
```

### Deployment Commands
```bash
# Build and deploy
mvn clean package -DskipTests
docker-compose up --build -d

# Monitor services
docker-compose ps
docker-compose logs -f banklite-app

# Health verification
curl http://localhost:8080/actuator/health
```

## 📊 API Usage Examples

### Create Account
```bash
curl -X POST http://localhost:8080/api/v1/accounts \
  -H "Content-Type: application/json" \
  -d '{
    "accountHolderName": "Alice Johnson",
    "balance": 1000.00,
    "currency": "USD"
  }'
```

### Response Example
```json
{
  "id": 6,
  "accountHolderName": "Alice Johnson",
  "accountNumber": "ACC1698765432006",
  "balance": 1000.00,
  "currency": "USD",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

## 📖 Interactive Documentation

Access comprehensive API documentation through Swagger UI:

**URL**: http://localhost:8080/swagger-ui.html

Features:
- Interactive endpoint testing
- Request/response schema validation
- Authentication examples
- Downloadable OpenAPI specification

## 🔍 Monitoring & Health

### Health Check Response
```json
{
  "status": "UP",
  "components": {
    "db": {
      "status": "UP",
      "details": {
        "database": "PostgreSQL",
        "validationQuery": "isValid()"
      }
    },
    "diskSpace": { "status": "UP" },
    "ping": { "status": "UP" }
  }
}
```

## 🎯 Interview Context & Research

### Yapily Research Deep Dive

This project was developed specifically for the Yapily interview process, incorporating research into Yapily's technology stack and Open Banking focus:

**Key Research Areas**:
- **Open Banking Infrastructure**: Understanding PSD2 compliance and API standardization
- **Financial Technology Stack**: Modern Java ecosystem for fintech applications
- **API-First Design**: RESTful principles aligned with banking industry standards
- **Security & Compliance**: Banking-grade security considerations and best practices

**Technical Alignment**:
- **Spring Boot Ecosystem**: Matches Yapily's Java-centric technology approach
- **Microservices Ready**: Containerized architecture supporting scalable deployments
- **API Documentation**: Comprehensive OpenAPI specification for developer experience
- **Testing Excellence**: Enterprise-grade testing strategies for financial applications

### Development Timeline
- **Research Phase**: Yapily technology analysis and Open Banking standards review
- **Implementation**: 4-5 hours focused development (as outlined in IMPLEMENTATION_PLAN.md)
- **Testing & Documentation**: Comprehensive test coverage and professional documentation
- **Containerization**: Production-ready Docker deployment configuration

## 🛠️ Project Structure

```
Banklite/
├── src/main/java/com/banklite/
│   ├── BankliteApplication.java          # Main application
│   ├── controller/AccountController.java # REST API layer
│   ├── service/AccountService.java       # Business logic
│   ├── repository/AccountRepository.java # Data access
│   ├── model/
│   │   ├── Account.java                  # JPA entity
│   │   ├── Currency.java                 # Currency enum
│   │   └── dto/                          # Data transfer objects
│   ├── config/                           # Application configuration
│   └── exception/                        # Error handling
├── src/test/java/                        # Comprehensive test suite
├── docker-compose.yml                    # Container orchestration
├── Dockerfile                            # Application containerization
├── init-db.sql                          # Database initialization
└── documentation/                        # Project documentation
    ├── README.md                         # Comprehensive project guide
    ├── IMPLEMENTATION_PLAN.md            # Technical implementation details
    ├── DATABASE_SETUP.md                 # Database configuration guide
    └── DOCKER_SETUP.md                   # Containerization guide
```

## 🤝 Professional Development Practices

### Code Quality
- **Java 21 Features**: Modern language features and best practices
- **Spring Boot Conventions**: Industry-standard framework patterns
- **Clean Architecture**: Separation of concerns and dependency injection
- **Validation**: Comprehensive input validation and error handling

### DevOps Excellence
- **Multi-Environment Support**: Development, testing, and production configurations
- **Container-First**: Docker-native deployment strategy
- **Health Monitoring**: Actuator endpoints for operational visibility
- **Documentation-Driven**: Comprehensive project documentation

### Testing Excellence
- **Test-Driven Development**: Tests written before implementation
- **Multiple Test Layers**: Unit, integration, and application context testing
- **Isolated Testing**: H2 in-memory database for clean test runs
- **Continuous Validation**: Automated test execution in build pipeline

## 📄 License & Purpose

This project is developed specifically for the Yapily technical interview process, demonstrating:
- Modern Java enterprise development capabilities
- RESTful API design excellence
- Production-ready deployment strategies
- Comprehensive testing and documentation practices

**Contact**: Available for technical discussion and code walkthrough during the interview process.

---

## 📷 API Documentation Screenshot

![BankLite Swagger UI](localhost_8080_swagger-ui_index.html.png)

*Interactive API documentation showcasing all available endpoints, request/response schemas, and testing capabilities*

---

**BankLite** - Production-ready banking API demonstrating enterprise Java development excellence for Yapily interview process 🏦