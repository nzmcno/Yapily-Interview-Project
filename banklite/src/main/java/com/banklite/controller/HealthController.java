package com.banklite.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * Health and Status Controller for BankLite Application
 * 
 * This controller provides basic health check and application status endpoints
 * for monitoring and verification purposes.
 * 
 * @author BankLite Team
 * @version 1.0
 * @since 2025-01-28
 */
@RestController
@RequestMapping("/api/v1")
@Tag(name = "Health & Monitoring", description = "Application health checks, metrics, and system status endpoints")
public class HealthController {

    /**
     * Basic application status endpoint.
     * 
     * This endpoint provides a simple health check to verify that the application
     * is running and responsive. It returns basic application information and
     * current timestamp.
     * 
     * @return ResponseEntity containing application status information
     */
    @GetMapping("/status")
    @Operation(summary = "Get application status", description = "Returns basic application health and status information including version, timestamp, and operational status.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Application is healthy and operational"),
            @ApiResponse(responseCode = "500", description = "Application encountered an error")
    })
    public ResponseEntity<Map<String, Object>> getApplicationStatus() {
        Map<String, Object> status = new HashMap<>();

        try {
            // Basic application information
            status.put("application", "BankLite Banking System");
            status.put("version", "1.0.0-SNAPSHOT");
            status.put("status", "UP");
            status.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

            // Runtime information
            Runtime runtime = Runtime.getRuntime();
            Map<String, Object> systemInfo = new HashMap<>();
            systemInfo.put("javaVersion", System.getProperty("java.version"));
            systemInfo.put("totalMemory", formatBytes(runtime.totalMemory()));
            systemInfo.put("freeMemory", formatBytes(runtime.freeMemory()));
            systemInfo.put("processors", runtime.availableProcessors());
            status.put("system", systemInfo);

            // API endpoints information
            Map<String, String> endpoints = new HashMap<>();
            endpoints.put("swagger", "/swagger-ui.html");
            endpoints.put("health", "/actuator/health");
            endpoints.put("info", "/actuator/info");
            endpoints.put("h2Console", "/h2-console");
            status.put("endpoints", endpoints);

            return ResponseEntity.ok(status);

        } catch (Exception e) {
            status.put("application", "BankLite Banking System");
            status.put("status", "DOWN");
            status.put("error", e.getMessage());
            status.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

            return ResponseEntity.status(500).body(status);
        }
    }

    /**
     * Welcome endpoint with API information.
     * 
     * This endpoint serves as the main entry point for the API, providing
     * welcome message and navigation information for developers.
     * 
     * @return ResponseEntity containing welcome message and API information
     */
    @GetMapping("/welcome")
    @Operation(summary = "Welcome message and API overview", description = "Provides a welcome message with overview of available API endpoints and documentation links.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Welcome message returned successfully")
    })
    public ResponseEntity<Map<String, Object>> getWelcomeMessage() {
        Map<String, Object> welcome = new HashMap<>();

        welcome.put("message", "Welcome to BankLite Banking API");
        welcome.put("description", "Modern Banking Application with comprehensive REST API");
        welcome.put("version", "1.0.0");
        welcome.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));

        // Available features
        Map<String, Object> features = new HashMap<>();
        features.put("accountManagement", "Create and manage bank accounts");
        features.put("transactions", "Process deposits, withdrawals, and transfers");
        features.put("balanceInquiry", "Check account balances in real-time");
        features.put("transactionHistory", "View detailed transaction logs");
        features.put("jwtAuthentication", "Secure JWT-based authentication");
        welcome.put("features", features);

        // Documentation links
        Map<String, String> documentation = new HashMap<>();
        documentation.put("swaggerUI", "/swagger-ui.html");
        documentation.put("openApiJson", "/v3/api-docs");
        documentation.put("openApiYaml", "/v3/api-docs.yaml");
        documentation.put("actuatorHealth", "/actuator/health");
        documentation.put("actuatorInfo", "/actuator/info");
        welcome.put("documentation", documentation);

        // Quick start guide
        Map<String, String> quickStart = new HashMap<>();
        quickStart.put("step1", "Visit /swagger-ui.html for interactive API documentation");
        quickStart.put("step2", "Check /actuator/health for application health status");
        quickStart.put("step3", "Use /h2-console for database access (development)");
        quickStart.put("step4", "Explore API endpoints using Swagger UI");
        welcome.put("quickStart", quickStart);

        return ResponseEntity.ok(welcome);
    }

    /**
     * Formats bytes into human-readable format.
     * 
     * @param bytes the number of bytes
     * @return formatted string (e.g., "256 MB", "1.5 GB")
     */
    private String formatBytes(long bytes) {
        if (bytes < 1024)
            return bytes + " B";
        if (bytes < 1024 * 1024)
            return String.format("%.2f KB", bytes / 1024.0);
        if (bytes < 1024 * 1024 * 1024)
            return String.format("%.2f MB", bytes / (1024.0 * 1024.0));
        return String.format("%.2f GB", bytes / (1024.0 * 1024.0 * 1024.0));
    }
}
