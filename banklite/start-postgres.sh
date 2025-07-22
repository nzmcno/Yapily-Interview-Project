#!/bin/bash

# BankLite PostgreSQL Startup Script

echo "🚀 Starting BankLite with PostgreSQL..."

# Load environment variables from .env file
export $(grep -v '^#' .env | xargs)

# Start the application with PostgreSQL profile
mvn spring-boot:run -Dspring.profiles.active=postgres

echo "✅ BankLite started with PostgreSQL!"