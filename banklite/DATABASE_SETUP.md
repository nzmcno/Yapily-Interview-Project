# PostgreSQL Database Setup Guide

## Overview
Bu guide PostgreSQL database'ini BankLite projesi için kurmak ve yapılandırmak için gerekli tüm adımları içerir.

---

## 1. PostgreSQL Kurulumu

### macOS (Homebrew)
```bash
# PostgreSQL kurulumu
brew install postgresql@15

# PostgreSQL servisini başlat
brew services start postgresql@15

# PostgreSQL'e bağlan
psql postgres
```

### Ubuntu/Debian
```bash
# PostgreSQL kurulumu
sudo apt update
sudo apt install postgresql postgresql-contrib

# PostgreSQL servisini başlat
sudo systemctl start postgresql
sudo systemctl enable postgresql

# PostgreSQL'e bağlan
sudo -u postgres psql
```

### Windows
```bash
# PostgreSQL'i resmi siteden indir ve kur
# https://www.postgresql.org/download/windows/

# pgAdmin veya psql ile bağlan
psql -U postgres
```

### Docker ile (Recommended)
```bash
# PostgreSQL container'ı çalıştır
docker run --name banklite-postgres \
  -e POSTGRES_DB=banklite \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=password \
  -p 5432:5432 \
  -d postgres:15

# Container'a bağlan
docker exec -it banklite-postgres psql -U postgres -d banklite
```

---

## 2. Database ve User Oluşturma

### PostgreSQL'e bağlandıktan sonra:

```sql
-- Database oluştur
CREATE DATABASE banklite;

-- Dedicated user oluştur (optional, güvenlik için)
CREATE USER banklite_user WITH ENCRYPTED PASSWORD 'banklite_password';

-- User'a database üzerinde tüm yetkiler ver
GRANT ALL PRIVILEGES ON DATABASE banklite TO banklite_user;

-- banklite database'ine geç
\c banklite;

-- Schema yetkilerini ver
GRANT ALL ON SCHEMA public TO banklite_user;

-- Mevcut ve gelecek tabloların yetkilerini ver
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO banklite_user;
GRANT ALL PRIVILEGES ON ALL SEQUENCES IN SCHEMA public TO banklite_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON TABLES TO banklite_user;
ALTER DEFAULT PRIVILEGES IN SCHEMA public GRANT ALL ON SEQUENCES TO banklite_user;

-- Database listesini kontrol et
\l

-- User listesini kontrol et
\du
```

---

## 3. Database Schema (Tables)

### Account Table
```sql
-- Account tablosunu oluştur
CREATE TABLE accounts (
    id BIGSERIAL PRIMARY KEY,
    account_holder_name VARCHAR(255) NOT NULL,
    account_number VARCHAR(50) NOT NULL UNIQUE,
    balance DECIMAL(19,2) NOT NULL DEFAULT 0.00,
    currency VARCHAR(3) NOT NULL CHECK (currency IN ('USD', 'EUR', 'GBP')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Indexes oluştur (performance için)
CREATE INDEX idx_accounts_account_number ON accounts(account_number);
CREATE INDEX idx_accounts_currency ON accounts(currency);
CREATE INDEX idx_accounts_created_at ON accounts(created_at);

-- Updated_at için trigger oluştur (auto-update)
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_accounts_updated_at 
    BEFORE UPDATE ON accounts 
    FOR EACH ROW 
    EXECUTE FUNCTION update_updated_at_column();
```

### Sample Data Insert
```sql
-- Test verisi ekle
INSERT INTO accounts (account_holder_name, account_number, balance, currency) VALUES
('John Doe', 'ACC1698765432001', 1500.00, 'USD'),
('Jane Smith', 'ACC1698765432002', 2750.50, 'EUR'),
('Bob Johnson', 'ACC1698765432003', 850.25, 'GBP'),
('Alice Brown', 'ACC1698765432004', 3200.00, 'USD'),
('Charlie Wilson', 'ACC1698765432005', 975.75, 'EUR');

-- Verileri kontrol et
SELECT * FROM accounts;
```

---

## 4. Spring Boot Configuration

### application.yml güncelle
```yaml
spring:
  application:
    name: banklite
  
  profiles:
    active: dev  # dev profile'ı kullan
  
  # Development için H2 (değişiklik yok)
  datasource:
    url: jdbc:h2:mem:banklite
    driverClassName: org.h2.Driver
    username: sa
    password: password

---
# PostgreSQL Production Profile
spring:
  config:
    activate:
      on-profile: postgres
  datasource:
    url: jdbc:postgresql://localhost:5432/banklite
    username: postgres
    password: password
    driver-class-name: org.postgresql.Driver
  jpa:
    hibernate:
      ddl-auto: validate  # Tablolar manuel oluşturulacak
    show-sql: true
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect
        format_sql: true
```

### PostgreSQL ile çalıştırma
```bash
# PostgreSQL profile ile uygulama başlat
mvn spring-boot:run -Dspring-boot.run.profiles=postgres

# Veya IDE'de VM Options:
-Dspring.profiles.active=postgres
```

---

## 5. Database Connection Test

### Test Scripts

#### Bağlantı Testi
```bash
# PostgreSQL bağlantısını test et
psql -h localhost -p 5432 -U postgres -d banklite -c "SELECT version();"

# Tablo varlığını kontrol et
psql -h localhost -p 5432 -U postgres -d banklite -c "\dt"

# Account verilerini kontrol et
psql -h localhost -p 5432 -U postgres -d banklite -c "SELECT COUNT(*) FROM accounts;"
```

#### Spring Boot ile Test
```bash
# Application başlat
mvn spring-boot:run -Dspring.profiles.active=postgres

# Health check
curl http://localhost:8080/actuator/health

# Database health
curl http://localhost:8080/actuator/health | jq '.components.db'
```

---

## 6. Docker Compose Setup (Alternative)

### docker-compose.yml
```yaml
version: '3.8'
services:
  postgres:
    image: postgres:15
    container_name: banklite-postgres
    environment:
      POSTGRES_DB: banklite
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: password
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
      - ./init-db.sql:/docker-entrypoint-initdb.d/init-db.sql
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U postgres"]
      interval: 30s
      timeout: 10s
      retries: 3

  app:
    build: .
    container_name: banklite-app
    ports:
      - "8080:8080"
    depends_on:
      postgres:
        condition: service_healthy
    environment:
      SPRING_PROFILES_ACTIVE: postgres
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/banklite
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: password

volumes:
  postgres_data:
```

### init-db.sql (Database initialization)
```sql
-- Bu dosya docker-compose ile otomatik çalışır
CREATE TABLE IF NOT EXISTS accounts (
    id BIGSERIAL PRIMARY KEY,
    account_holder_name VARCHAR(255) NOT NULL,
    account_number VARCHAR(50) NOT NULL UNIQUE,
    balance DECIMAL(19,2) NOT NULL DEFAULT 0.00,
    currency VARCHAR(3) NOT NULL CHECK (currency IN ('USD', 'EUR', 'GBP')),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Indexes
CREATE INDEX IF NOT EXISTS idx_accounts_account_number ON accounts(account_number);
CREATE INDEX IF NOT EXISTS idx_accounts_currency ON accounts(currency);

-- Trigger function
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

-- Trigger
DROP TRIGGER IF EXISTS update_accounts_updated_at ON accounts;
CREATE TRIGGER update_accounts_updated_at 
    BEFORE UPDATE ON accounts 
    FOR EACH ROW 
    EXECUTE FUNCTION update_updated_at_column();

-- Sample data
INSERT INTO accounts (account_holder_name, account_number, balance, currency) VALUES
('John Doe', 'ACC1698765432001', 1500.00, 'USD'),
('Jane Smith', 'ACC1698765432002', 2750.50, 'EUR'),
('Bob Johnson', 'ACC1698765432003', 850.25, 'GBP')
ON CONFLICT (account_number) DO NOTHING;
```

---

## 7. Database Maintenance

### Backup
```bash
# Database backup
pg_dump -h localhost -U postgres banklite > banklite_backup.sql

# Docker ile backup
docker exec banklite-postgres pg_dump -U postgres banklite > banklite_backup.sql
```

### Restore
```bash
# Database restore
psql -h localhost -U postgres banklite < banklite_backup.sql

# Docker ile restore
docker exec -i banklite-postgres psql -U postgres banklite < banklite_backup.sql
```

### Reset Database
```bash
# Tüm verileri sil
psql -h localhost -U postgres banklite -c "TRUNCATE TABLE accounts RESTART IDENTITY CASCADE;"

# Tabloları tamamen sil
psql -h localhost -U postgres banklite -c "DROP TABLE IF EXISTS accounts CASCADE;"
```

---

## 8. Troubleshooting

### Common Issues

#### Connection Refused
```bash
# PostgreSQL servisinin çalıştığını kontrol et
sudo systemctl status postgresql  # Linux
brew services list | grep postgresql  # macOS

# Port'un açık olduğunu kontrol et
netstat -an | grep 5432
```

#### Authentication Failed
```bash
# pg_hba.conf dosyasını kontrol et
sudo find / -name "pg_hba.conf" 2>/dev/null

# Local connections için trust ekle (development only)
echo "local all all trust" | sudo tee -a /etc/postgresql/15/main/pg_hba.conf
sudo systemctl restart postgresql
```

#### Permission Denied
```sql
-- User yetkilerini tekrar ver
GRANT ALL PRIVILEGES ON DATABASE banklite TO postgres;
GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO postgres;
```

---

## 9. Quick Start Commands

```bash
# 1. PostgreSQL başlat (Docker)
docker run --name banklite-postgres \
  -e POSTGRES_DB=banklite \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=password \
  -p 5432:5432 \
  -d postgres:15

# 2. Database setup
docker exec -it banklite-postgres psql -U postgres -d banklite -c "
CREATE TABLE accounts (
    id BIGSERIAL PRIMARY KEY,
    account_holder_name VARCHAR(255) NOT NULL,
    account_number VARCHAR(50) NOT NULL UNIQUE,
    balance DECIMAL(19,2) NOT NULL DEFAULT 0.00,
    currency VARCHAR(3) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);"

# 3. Spring Boot ile çalıştır
mvn spring-boot:run -Dspring.profiles.active=postgres

# 4. Test et
curl http://localhost:8080/actuator/health
```

Bu guide ile PostgreSQL database'ini tamamen kurabilir ve BankLite projesi için hazır hale getirebilirsin!