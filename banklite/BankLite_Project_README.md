# BankLite - Open Banking Demo

**30-minute production-ready Open Banking demo** showcasing Spring Boot 3 and external bank integration.


## Core Features

- **Account CRUD** with validation and multi-currency support
- **External Bank Integration** using WebClient
- **Health Monitoring** and Swagger documentation

## Tech Stack

- **Java 21** + **Spring Boot 3**
- **WebClient** for external APIs
- **H2 Database** (demo)
- **Docker** containerization



## API Endpoints

- `GET|POST|PUT|DELETE /api/v1/accounts` - Account CRUD
- `POST /api/v1/accounts/sync` - External bank sync
- `GET /health` - Health check
- `GET /swagger-ui.html` - API documentation


## Quick Start

```bash
# Clone and run
git clone <repository-url>
cd banklite
mvn spring-boot:run

# Access Swagger UI
open http://localhost:8080/swagger-ui.html

# Test API endpoints
curl -X GET http://localhost:8080/api/v1/accounts
```

## Implementation (10 hours total)

- **Day 1**: Spring Boot + Account CRUD (4h)
- **Day 2**: External integration + WebClient (4h) 
- **Day 3**: Testing + Polish (2h)


## Docker

```bash
mvn clean package
docker build -t banklite .
docker run -p 8080:8080 banklite
```

## Requirements Alignment

✅ **Spring Boot & Java 11+** - Using Spring Boot 3 + Java 21  
✅ **Test-driven development** - JUnit 5 + Testcontainers  
✅ **HTTP & RESTful APIs** - Full REST API with OpenAPI docs  
✅ **Docker packaging** - Complete containerization setup  

**Demonstrates**: Open Banking infrastructure patterns, external API integration, high-availability design

---

**Yazılım geliştirme becerilerini Open Banking altyapısıyla sergilemek için tasarlanmış, hızlı kurulum ve demo-dostu proje.**
