package com.banklite.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Web Configuration for BankLite Application
 * 
 * This configuration class handles web-related settings including CORS
 * configuration
 * and other web MVC customizations for the BankLite banking system.
 * 
 * @author BankLite Team
 * @version 1.0
 * @since 2025-01-28
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    /**
     * Configure CORS settings for the application.
     * 
     * This method sets up Cross-Origin Resource Sharing (CORS) configuration
     * to allow frontend applications to communicate with the API from different
     * domains.
     * 
     * @param registry the CorsRegistry to configure
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3000", "http://localhost:4200", "http://localhost:8081")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);

        registry.addMapping("/actuator/**")
                .allowedOrigins("*")
                .allowedMethods("GET")
                .allowedHeaders("*")
                .maxAge(3600);
    }
}
