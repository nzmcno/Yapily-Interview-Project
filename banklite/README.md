# BankLite - Simple Banking REST API

A lightweight, containerized banking account management system built with Spring Boot 3, PostgreSQL, and Docker. This project provides essential CRUD operations for account management with comprehensive testing and documentation.

## 🏗️ Architecture

**Tech Stack:**
- **Java 21** with Spring Boot 3.x
- **PostgreSQL 15** for production database
- **H2** for development and testing
- **Docker & Docker Compose** for containerization
- **Maven** for dependency management
- **JUnit 5 & Mockito** for testing
- **OpenAPI/Swagger** for API documentation

## 🚀 Quick Start

### Prerequisites
- Java 21+
- Maven 3.8+
- Docker & Docker Compose
- Git

### 1. Clone Repository
```bash
git clone <repository-url>
cd Banklite
```

### 2. Run with Docker (Recommended)
```bash
# Build the JAR file
mvn clean package -DskipTests

# Start all services
docker-compose up --build -d

# Verify deployment
curl http://localhost:8080/actuator/health
```

### 3. Run Locally with H2 Database
```bash
# Start application with development profile
mvn spring-boot:run

# Or with specific profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### 4. Run with Local PostgreSQL
```bash
# Set up PostgreSQL database (see DATABASE_SETUP.md)
# Update .env file with your PostgreSQL credentials
mvn spring-boot:run -Dspring-boot.run.profiles=postgres
```

## 📋 API Endpoints

### Base URL
- **Local**: `http://localhost:8080`
- **Docker**: `http://localhost:8080`

### Account Management
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/api/v1/accounts` | Get all accounts |
| `GET` | `/api/v1/accounts/{id}` | Get account by ID |
| `POST` | `/api/v1/accounts` | Create new account |
| `PUT` | `/api/v1/accounts/{id}` | Update account |
| `DELETE` | `/api/v1/accounts/{id}` | Delete account |

### System Endpoints
| Method | Endpoint | Description |
|--------|----------|-------------|
| `GET` | `/actuator/health` | System health check |
| `GET` | `/swagger-ui.html` | API documentation |

## 🔧 Configuration

### Environment Profiles

**Development Profile (`dev`)** - Default
- Uses H2 in-memory database
- Automatic table creation
- Ideal for development and testing

**PostgreSQL Profile (`postgres`)**
- Uses PostgreSQL database
- Requires database setup
- Production-ready configuration

### Environment Variables (.env)
```env
# Database Configuration
DB_HOST=localhost
DB_PORT=5432
DB_NAME=banklite
DB_USERNAME=banklite_user
DB_PASSWORD=banklite_password

# Spring Profiles
SPRING_PROFILES_ACTIVE=postgres

# Server Configuration
SERVER_PORT=8080

# Logging Configuration
LOG_LEVEL_ROOT=INFO
LOG_LEVEL_BANKLITE=DEBUG
LOG_LEVEL_SQL=false
```

## 💾 Database Setup

### Option 1: Docker PostgreSQL (Included in docker-compose)
The `docker-compose.yml` automatically sets up PostgreSQL with sample data.

### Option 2: Manual PostgreSQL Setup
See detailed instructions in [DATABASE_SETUP.md](DATABASE_SETUP.md)

### Database Schema
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

## 🧪 Testing

### Run All Tests
```bash
mvn test
```

### Test Coverage
- **Unit Tests**: Service layer with Mockito
- **Integration Tests**: Full REST API with TestRestTemplate
- **Test Database**: H2 in-memory for isolated testing

### Test Results Summary
```
Tests run: 13, Failures: 0, Errors: 0, Skipped: 0
├── AccountServiceTest: 7 tests
├── AccountControllerIntegrationTest: 5 tests
└── BankliteApplicationTests: 1 test
```

## 🐳 Docker Deployment

### Container Architecture
```yaml
services:
  postgres:     # PostgreSQL 15 database
  app:          # BankLite Spring Boot application
```

### Build and Deploy
```bash
# Build JAR file
mvn clean package -DskipTests

# Start services
docker-compose up --build -d

# Check status
docker-compose ps

# View logs
docker-compose logs app
```

### Container Health Checks
- **PostgreSQL**: `pg_isready` every 30s
- **Application**: `/actuator/health` every 30s

## 📊 Sample Data

When using PostgreSQL, the database is automatically populated with sample accounts:

```json
[
  {
    "id": 1,
    "accountHolderName": "John Doe",
    "accountNumber": "ACC1698765432001",
    "balance": 1500.00,
    "currency": "USD"
  },
  {
    "id": 2,
    "accountHolderName": "Jane Smith", 
    "accountNumber": "ACC1698765432002",
    "balance": 2750.50,
    "currency": "EUR"
  }
  // ... 3 more accounts
]
```

## 🌐 API Usage Examples

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

### Get All Accounts
```bash
curl http://localhost:8080/api/v1/accounts
```

### Get Account by ID
```bash
curl http://localhost:8080/api/v1/accounts/1
```

### Update Account
```bash
curl -X PUT http://localhost:8080/api/v1/accounts/1 \
  -H "Content-Type: application/json" \
  -d '{
    "accountHolderName": "Alice Smith",
    "balance": 1500.00,
    "currency": "EUR"
  }'
```

### Delete Account
```bash
curl -X DELETE http://localhost:8080/api/v1/accounts/1
```

## 📖 API Documentation

### Swagger UI
Access interactive API documentation at:
- **URL**: http://localhost:8080/swagger-ui.html
- **Features**: Try out endpoints, view schemas, download OpenAPI spec

### OpenAPI Specification
- **Endpoint**: http://localhost:8080/v3/api-docs
- **Format**: JSON format for integration with API tools

## 🔍 Monitoring & Health

### Health Check Endpoint
```bash
curl http://localhost:8080/actuator/health
```

### Sample Health Response
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

## 🛠️ Development

### Project Structure
```
src/main/java/com/banklite/
├── BankliteApplication.java          # Main application class
├── controller/
│   ├── AccountController.java        # REST API endpoints
│   └── HealthController.java         # Health check endpoint
├── service/
│   └── AccountService.java           # Business logic layer
├── repository/
│   └── AccountRepository.java        # Data access layer
├── model/
│   ├── Account.java                  # JPA entity
│   ├── Currency.java                 # Currency enum
│   └── dto/
│       ├── AccountRequest.java       # Request DTO
│       ├── AccountResponse.java      # Response DTO
│       └── ErrorResponse.java        # Error DTO
├── config/
│   ├── OpenApiConfig.java           # Swagger configuration
│   └── WebConfig.java               # CORS configuration
└── exception/
    ├── AccountNotFoundException.java # Custom exception
    └── GlobalExceptionHandler.java  # Global error handling (disabled)
```

### Build Commands
```bash
# Compile
mvn compile

# Run tests
mvn test

# Package JAR
mvn package

# Clean build
mvn clean package

# Skip tests
mvn package -DskipTests

# Run with specific profile
mvn spring-boot:run -Dspring-boot.run.profiles=postgres
```

## 🚦 Troubleshooting

### Common Issues

**Port 8080 already in use:**
```bash
# Find process using port 8080
lsof -i :8080

# Kill process
kill -9 <PID>
```

**PostgreSQL connection failed:**
```bash
# Check PostgreSQL status
docker-compose logs postgres

# Test database connection
docker exec banklite-postgres pg_isready -U banklite_user -d banklite
```

**Docker build failed:**
```bash
# Clean Docker images
docker system prune -a

# Rebuild without cache
docker-compose build --no-cache
```

**Tests failing:**
```bash
# Run tests with verbose output
mvn test -X

# Run specific test class
mvn test -Dtest=AccountServiceTest
```

### Logs and Debugging

**Application Logs:**
```bash
# Local development
tail -f logs/spring.log

# Docker logs
docker-compose logs -f app
```

**Database Logs:**
```bash
# PostgreSQL logs
docker-compose logs postgres
```

## 🔒 Security Considerations

### Development
- H2 console disabled in production
- Basic authentication removed for simplicity
- CORS enabled for localhost origins

### Production Recommendations
- Enable HTTPS/TLS
- Add authentication (JWT/OAuth2)
- Implement rate limiting
- Use environment-specific secrets
- Enable audit logging
- Configure firewall rules

## 📈 Performance

### Current Optimizations
- Database indexes on frequently queried columns
- Connection pooling via HikariCP
- JPA query optimization
- Docker image optimization

### Monitoring
- Spring Boot Actuator metrics
- Health check endpoints
- Container health monitoring
- Database connection monitoring

## 🤝 Contributing

### Development Workflow
1. Clone repository
2. Create feature branch
3. Make changes with tests
4. Run full test suite
5. Submit pull request

### Code Standards
- Java 21 features
- Spring Boot conventions
- REST API best practices
- Comprehensive testing
- Clear documentation

## 📄 License

This project is created for educational and demonstration purposes.

## 📞 Support

For issues and questions:
- Check [DATABASE_SETUP.md](DATABASE_SETUP.md) for database setup
- Check [DOCKER_SETUP.md](DOCKER_SETUP.md) for Docker deployment
- Review [IMPLEMENTATION_PLAN.md](IMPLEMENTATION_PLAN.md) for technical details

---

**BankLite** - Simple, reliable, and production-ready banking API solution! 🏦