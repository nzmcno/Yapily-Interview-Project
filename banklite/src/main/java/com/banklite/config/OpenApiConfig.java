package com.banklite.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * OpenAPI/Swagger Configuration for BankLite Application
 * 
 * This configuration class sets up comprehensive API documentation using
 * OpenAPI 3.0
 * and provides a user-friendly Swagger UI interface for testing endpoints.
 * 
 * Access points:
 * - Swagger UI: http://localhost:8080/swagger-ui.html
 * - OpenAPI JSON: http://localhost:8080/v3/api-docs
 * - OpenAPI YAML: http://localhost:8080/v3/api-docs.yaml
 * 
 * @author BankLite Team
 * @version 1.0
 * @since 2025-01-28
 */
@Configuration
public class OpenApiConfig {

    /**
     * Configures the OpenAPI specification for the BankLite application.
     * 
     * This bean defines comprehensive API documentation including:
     * - Application metadata and version information
     * - Contact details and licensing
     * - Server configurations for different environments
     * - API tags for logical grouping of endpoints
     * 
     * @return OpenAPI configuration object
     */
    @Bean
    public OpenAPI bankLiteOpenAPI() {
        return new OpenAPI()
                .info(createApiInfo())
                .servers(createServerList())
                .tags(createApiTags());
    }

    /**
     * Creates detailed API information metadata.
     * 
     * @return Info object containing API metadata
     */
    private Info createApiInfo() {
        return new Info()
                .title("BankLite API")
                .description("""
                        ## BankLite - Modern Banking Application API

                        A comprehensive REST API for modern banking operations including:

                        ### 🏦 Core Banking Features
                        - **Account Management**: Create, view, and manage bank accounts
                        - **Transaction Processing**: Handle deposits, withdrawals, and transfers
                        - **Balance Inquiries**: Real-time account balance checking
                        - **Transaction History**: Detailed transaction logs and filtering

                        ### 🔐 Security & Authentication
                        - **JWT Authentication**: Secure token-based authentication
                        - **Role-based Access**: Different permissions for users and admins
                        - **Data Encryption**: Secure handling of sensitive financial data

                        ### 🚀 Enterprise Features
                        - **RESTful Design**: Clean, predictable API endpoints
                        - **Real-time Processing**: Immediate transaction processing
                        - **Comprehensive Logging**: Full audit trail for all operations
                        - **Health Monitoring**: Built-in health checks and metrics

                        ### 📊 Technical Stack
                        - **Spring Boot 3.5.4**: Latest enterprise framework
                        - **Java 21**: Modern LTS version with latest features
                        - **PostgreSQL**: Production-ready database
                        - **H2**: In-memory database for development/testing

                        ### 🔗 Quick Links
                        - Health Check: `/actuator/health`
                        - Application Info: `/actuator/info`
                        - H2 Console (Dev): `/h2-console`
                        """)
                .version("1.0.0")
                .contact(createContactInfo())
                .license(createLicenseInfo());
    }

    /**
     * Creates contact information for the API.
     * 
     * @return Contact object with team details
     */
    private Contact createContactInfo() {
        return new Contact()
                .name("BankLite Development Team")
                .email("api-support@banklite.com")
                .url("https://banklite.com/support");
    }

    /**
     * Creates license information for the API.
     * 
     * @return License object with licensing details
     */
    private License createLicenseInfo() {
        return new License()
                .name("MIT License")
                .url("https://opensource.org/licenses/MIT");
    }

    /**
     * Creates server configurations for different environments.
     * 
     * @return List of Server objects for various deployment environments
     */
    private List<Server> createServerList() {
        Server developmentServer = new Server()
                .url("http://localhost:8080")
                .description("Development Server");

        Server stagingServer = new Server()
                .url("https://staging-api.banklite.com")
                .description("Staging Server");

        Server productionServer = new Server()
                .url("https://api.banklite.com")
                .description("Production Server");

        return List.of(developmentServer, stagingServer, productionServer);
    }

    /**
     * Creates API tags for logical grouping of endpoints.
     * 
     * @return List of Tag objects for organizing API endpoints
     */
    private List<Tag> createApiTags() {
        return List.of(
                new Tag()
                        .name("Authentication")
                        .description("User authentication and authorization endpoints"),

                new Tag()
                        .name("Account Management")
                        .description("Bank account creation, viewing, and management operations"),

                new Tag()
                        .name("Transactions")
                        .description("Financial transaction processing including deposits, withdrawals, and transfers"),

                new Tag()
                        .name("Balance Inquiry")
                        .description("Account balance checking and balance history operations"),

                new Tag()
                        .name("Transaction History")
                        .description("Transaction logs, filtering, and reporting operations"),

                new Tag()
                        .name("Health & Monitoring")
                        .description("Application health checks, metrics, and system status endpoints"),

                new Tag()
                        .name("Administration")
                        .description("Administrative operations and system management endpoints"));
    }
}
