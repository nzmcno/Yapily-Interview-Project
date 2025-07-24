# BankLite: Simple REST API Implementation Plan

## Overview
Basic REST API for account management with PostgreSQL database. Simple CRUD operations with Docker support and basic testing.

**Duration**: 4-5 hours  
**Tech Stack**: Java 21, Spring Boot 3, PostgreSQL, Docker  

---

## Project Structure
```
src/main/java/com/banklite/
├── BankliteApplication.java
├── controller/
│   └── AccountController.java
├── service/
│   └── AccountService.java
├── repository/
│   └── AccountRepository.java
├── model/
│   ├── Account.java
│   └── dto/
│       ├── AccountRequest.java
│       └── AccountResponse.java
└── config/
    └── OpenApiConfig.java
```

---

## Implementation Steps

### 1. Project Setup ✅ COMPLETED

#### pom.xml dependencies ✅
Current dependencies include:
- ✅ spring-boot-starter-web
- ✅ spring-boot-starter-data-jpa  
- ✅ spring-boot-starter-validation
- ✅ postgresql driver
- ✅ springdoc-openapi (version 2.6.0)
- ✅ h2 database (for development)
- ✅ lombok
- ✅ spring-boot-starter-test
- ✅ testcontainers
- ✅ wiremock

#### application.yml ✅
Current configuration includes:
- ✅ H2 database for development (jdbc:h2:mem:banklite)
- ✅ PostgreSQL for production profile
- ✅ Swagger UI at /swagger-ui.html
- ✅ Actuator endpoints enabled
- ✅ Multi-profile setup (dev/prod)

**Status**: ✅ Application is running and Swagger is accessible at http://localhost:8080/swagger-ui.html

### 2. Entity & Model ✅ COMPLETED (45 minutes)

#### Account Entity ✅
**File**: `src/main/java/com/banklite/model/Account.java`
- ✅ JPA annotations with PostgreSQL mapping
- ✅ Lombok annotations for clean code
- ✅ Validation constraints
- ✅ CreationTimestamp and UpdateTimestamp
- ✅ BigDecimal for precise currency handling

#### Currency Enum ✅
**File**: `src/main/java/com/banklite/model/Currency.java`
- ✅ USD, EUR, GBP support
- ✅ Enum stored as STRING in database

#### DTOs ✅
**AccountRequest** - `src/main/java/com/banklite/model/dto/AccountRequest.java`
- ✅ Validation annotations with custom messages
- ✅ @NotBlank for accountHolderName
- ✅ @DecimalMin("0.0") for balance validation
- ✅ @NotNull for required fields
- ✅ Lombok annotations for clean code

**AccountResponse** - `src/main/java/com/banklite/model/dto/AccountResponse.java`  
- ✅ All necessary fields for API responses
- ✅ LocalDateTime for timestamp fields
- ✅ Clean DTO without JPA annotations
- ✅ Lombok annotations for clean code

### 3. Repository Layer ✅ COMPLETED (15 minutes)

**File**: `src/main/java/com/banklite/repository/AccountRepository.java`
- ✅ Extends JpaRepository<Account, Long> for basic CRUD operations
- ✅ findByAccountNumber() for unique account lookup
- ✅ findByAccountHolderNameContaining() for search functionality
- ✅ findByCurrency() for filtering by currency type
- ✅ Spring Data JPA auto-implementation
- ✅ @Repository annotation for component scanning

**Status**: ✅ Repository successfully registered - "Found 1 JPA repository interface"

### 4. Service Layer ✅ COMPLETED (30 minutes)

**File**: `src/main/java/com/banklite/service/AccountService.java`

#### ✅ Business Logic Methods:
- **createAccount()** - Creates new account with auto-generated account number
- **getAccount()** - Retrieves account by ID with error handling  
- **getAllAccounts()** - Returns all accounts as response DTOs
- **updateAccount()** - Updates existing account details
- **deleteAccount()** - Soft validation before deletion

#### ✅ Technical Features:
- **@Service** annotation for Spring component scanning
- **@Transactional** for database transaction management
- **Constructor injection** for AccountRepository dependency
- **generateAccountNumber()** - Unique ID generation using timestamp
- **mapToResponse()** - Entity to DTO mapping utility
- **Exception handling** - RuntimeException for not found cases

#### ✅ Database Integration:
- **PostgreSQL compatible** - Works with existing accounts table
- **JPA Repository methods** - save(), findById(), findAll(), deleteById()
- **Entity mapping** - Account entity to AccountResponse DTO
- **Transaction safety** - All operations are transactional

### 5. REST Controller (45 minutes)

```java
@RestController
@RequestMapping("/api/v1/accounts")
@Tag(name = "Account Management")
public class AccountController {
    
    private final AccountService accountService;
    
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }
    
    @PostMapping
    @Operation(summary = "Create new account")
    public ResponseEntity<AccountResponse> createAccount(@Valid @RequestBody AccountRequest request) {
        AccountResponse response = accountService.createAccount(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get account by ID")
    public ResponseEntity<AccountResponse> getAccount(@PathVariable Long id) {
        AccountResponse response = accountService.getAccount(id);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping
    @Operation(summary = "Get all accounts")
    public ResponseEntity<List<AccountResponse>> getAllAccounts() {
        List<AccountResponse> accounts = accountService.getAllAccounts();
        return ResponseEntity.ok(accounts);
    }
    
    @PutMapping("/{id}")
    @Operation(summary = "Update account")
    public ResponseEntity<AccountResponse> updateAccount(
            @PathVariable Long id, 
            @Valid @RequestBody AccountRequest request) {
        AccountResponse response = accountService.updateAccount(id, request);
        return ResponseEntity.ok(response);
    }
    
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete account")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        accountService.deleteAccount(id);
        return ResponseEntity.noContent().build();
    }
}
```

### 6. Configuration (15 minutes)

#### Swagger Configuration
```java
@Configuration
public class OpenApiConfig {
    
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("BankLite API")
                        .version("1.0")
                        .description("Simple banking account management API"));
    }
}
```

### 7. Testing (1 hour)

#### Unit Tests
```java
@ExtendWith(MockitoExtension.class)
class AccountServiceTest {
    
    @Mock
    private AccountRepository accountRepository;
    
    @InjectMocks
    private AccountService accountService;
    
    @Test
    void shouldCreateAccount() {
        // Given
        AccountRequest request = new AccountRequest();
        request.setAccountHolderName("John Doe");
        request.setBalance(new BigDecimal("1000.00"));
        request.setCurrency(Currency.USD);
        
        Account savedAccount = new Account();
        savedAccount.setId(1L);
        savedAccount.setAccountHolderName("John Doe");
        savedAccount.setBalance(new BigDecimal("1000.00"));
        savedAccount.setCurrency(Currency.USD);
        
        when(accountRepository.save(any(Account.class))).thenReturn(savedAccount);
        
        // When
        AccountResponse response = accountService.createAccount(request);
        
        // Then
        assertThat(response.getId()).isEqualTo(1L);
        assertThat(response.getAccountHolderName()).isEqualTo("John Doe");
        assertThat(response.getBalance()).isEqualTo(new BigDecimal("1000.00"));
    }
}
```

#### Integration Tests
```java
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(properties = {
    "spring.datasource.url=jdbc:h2:mem:testdb",
    "spring.jpa.hibernate.ddl-auto=create-drop"
})
class AccountControllerIntegrationTest {
    
    @Autowired
    private TestRestTemplate restTemplate;
    
    @Test
    void shouldCreateAndGetAccount() {
        // Create account
        AccountRequest request = new AccountRequest();
        request.setAccountHolderName("John Doe");
        request.setBalance(new BigDecimal("1000.00"));
        request.setCurrency(Currency.USD);
        
        ResponseEntity<AccountResponse> createResponse = restTemplate.postForEntity(
            "/api/v1/accounts", request, AccountResponse.class);
        
        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(createResponse.getBody().getAccountHolderName()).isEqualTo("John Doe");
        
        // Get account
        Long accountId = createResponse.getBody().getId();
        ResponseEntity<AccountResponse> getResponse = restTemplate.getForEntity(
            "/api/v1/accounts/" + accountId, AccountResponse.class);
        
        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(getResponse.getBody().getId()).isEqualTo(accountId);
    }
}
```

### 8. Docker Setup (30 minutes)

#### Dockerfile
```dockerfile
FROM openjdk:21-jre-slim

WORKDIR /app

COPY target/banklite-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

#### docker-compose.yml
```yaml
version: '3.8'
services:
  postgres:
    image: postgres:15
    environment:
      POSTGRES_DB: banklite
      POSTGRES_USER: postgres
      POSTGRES_PASSWORD: password
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data

  app:
    build: .
    ports:
      - "8080:8080"
    depends_on:
      - postgres
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/banklite
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: password

volumes:
  postgres_data:
```

---

## Quick Start Commands

```bash
# Build the application
mvn clean package

# Run with Docker Compose
docker-compose up --build

# Test the API
curl -X POST http://localhost:8080/api/v1/accounts \
  -H "Content-Type: application/json" \
  -d '{
    "accountHolderName": "John Doe",
    "balance": 1000.00,
    "currency": "USD"
  }'

# Get all accounts
curl http://localhost:8080/api/v1/accounts

# Access Swagger UI
open http://localhost:8080/swagger-ui.html
```

---

## API Endpoints

- **POST** `/api/v1/accounts` - Create account
- **GET** `/api/v1/accounts` - Get all accounts  
- **GET** `/api/v1/accounts/{id}` - Get account by ID
- **PUT** `/api/v1/accounts/{id}` - Update account
- **DELETE** `/api/v1/accounts/{id}` - Delete account

---

## Testing Checklist

- [ ] Unit tests for service layer
- [ ] Integration tests for REST endpoints
- [ ] API documentation with Swagger
- [ ] Docker containerization working
- [ ] PostgreSQL database connection
- [ ] All CRUD operations functional

This simplified plan focuses on the essentials: basic REST API, PostgreSQL integration, Docker support, and basic testing.