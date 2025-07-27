# Docker Setup Guide

## Overview

Bu guide BankLite projesini Docker ve docker-compose ile çalıştırmak için gerekli tüm bilgileri içerir.

## Files Created

### 1. Dockerfile

- **Eclipse Temurin 21 JRE** base image
- Health check enabled
- Curl pre-installed for health monitoring
- Port 8080 exposed

### 2. docker-compose.yml

- **PostgreSQL 15** database service
- **BankLite application** service
- Automatic database initialization
- Health checks for both services
- Custom network configuration
- Environment variables from .env file

### 3. init-db.sql

- Automatic database setup script
- Creates `accounts` table with indexes
- Inserts sample test data
- Sets up user permissions
- Auto-triggers for updated_at timestamp

### 4. .dockerignore

- Excludes unnecessary files from Docker build
- Keeps only the JAR file from target directory
- Reduces Docker image size

## Quick Start

### 1. Build and Run

```bash
# Build JAR file
mvn clean package -DskipTests

# Start all services
docker-compose up --build -d

# Check status
docker-compose ps
```

### 2. Verify Setup

```bash
# Health check
curl http://localhost:8080/actuator/health

# Get all accounts
curl http://localhost:8080/api/v1/accounts

# Create new account
curl -X POST http://localhost:8080/api/v1/accounts \
  -H "Content-Type: application/json" \
  -d '{
    "accountHolderName": "Test User",
    "balance": 1000.00,
    "currency": "USD"
  }'
```

### 3. Access Services

- **API**: http://localhost:8080/api/v1/accounts
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **Health Check**: http://localhost:8080/actuator/health
- **PostgreSQL**: localhost:5432 (banklite/banklite_user/banklite_password)

## Environment Configuration

Docker compose uses the following environment variables:

```yaml
# Database Configuration
DB_HOST: postgres
DB_PORT: 5432
DB_NAME: banklite
DB_USERNAME: banklite_user
DB_PASSWORD: banklite_password

# Spring Profiles
SPRING_PROFILES_ACTIVE: postgres

# Server Configuration
SERVER_PORT: 8080

# Logging Configuration
LOG_LEVEL_ROOT: INFO
LOG_LEVEL_BANKLITE: DEBUG
LOG_LEVEL_SQL: false
```

## Sample Data

The database is automatically initialized with 5 test accounts:

- John Doe - $1,500.00 USD
- Jane Smith - €2,750.50 EUR
- Bob Johnson - £850.25 GBP
- Alice Brown - $3,200.00 USD
- Charlie Wilson - €975.75 EUR

## Container Management

### Stop Services

```bash
docker-compose down
```

### Stop and Remove Volumes

```bash
docker-compose down -v
```

### View Logs

```bash
# All services
docker-compose logs

# Specific service
docker-compose logs app
docker-compose logs postgres
```

### Restart Services

```bash
docker-compose restart
```

## Database Access

### Connect to PostgreSQL Container

```bash
docker exec -it banklite-postgres psql -U banklite_user -d banklite
```

### View Database Data

```sql
-- List all accounts
SELECT * FROM accounts;

-- Check table structure
\d accounts;

-- View database info
\l
\dt
```

## Health Monitoring

Both services have health checks:

### PostgreSQL Health Check

- Runs every 30 seconds
- Uses `pg_isready` command
- Retries 3 times with 10s timeout

### Application Health Check

- Runs every 30 seconds
- Uses `/actuator/health` endpoint
- 60 second startup period
- Retries 3 times with 10s timeout

## Network Configuration

Custom bridge network `banklite-network` enables:

- Service-to-service communication
- DNS resolution between containers
- Isolated network environment

## Volume Management

Persistent volume `postgres_data` stores:

- PostgreSQL database files
- Survives container restarts
- Located at `/var/lib/postgresql/data`

## Troubleshooting

### Container Won't Start

```bash
# Check logs
docker-compose logs app
docker-compose logs postgres

# Verify images
docker images | grep banklite
docker images | grep postgres
```

### Database Connection Issues

```bash
# Test database connectivity
docker exec banklite-postgres pg_isready -U banklite_user -d banklite

# Check database logs
docker-compose logs postgres
```

### Application Issues

```bash
# Check application logs
docker-compose logs app

# Verify health
curl http://localhost:8080/actuator/health

# Test API directly
curl http://localhost:8080/api/v1/accounts
```

### Port Conflicts

```bash
# Check what's using port 8080
lsof -i :8080

# Check what's using port 5432
lsof -i :5432
```

## Production Considerations

For production deployment:

1. **Security**

   - Change default passwords
   - Use secrets management
   - Enable SSL/TLS
   - Restrict network access

2. **Performance**

   - Configure JVM heap size
   - Tune PostgreSQL settings
   - Add connection pooling
   - Set up monitoring

3. **Scaling**

   - Use external PostgreSQL
   - Add load balancer
   - Configure multiple app instances
   - Implement health checks

4. **Backup**
   - Schedule database backups
   - Monitor disk space
   - Test restore procedures
