#!/bin/bash

# EduManager System - Start Script
# This script starts the entire application stack using Docker Compose

echo "==================================="
echo "EduManager System - Starting..."
echo "==================================="

# Check if docker-compose exists (v1) or use docker compose (v2)
if command -v docker-compose &> /dev/null; then
    DOCKER_COMPOSE="docker-compose"
elif docker compose version &> /dev/null; then
    DOCKER_COMPOSE="docker compose"
else
    echo "Error: Neither 'docker-compose' nor 'docker compose' is available"
    exit 1
fi

# Build and start all services
echo "Building and starting services..."
$DOCKER_COMPOSE up --build -d

echo ""
echo "==================================="
echo "Services are starting up!"
echo "==================================="
echo ""
echo "Access the application at:"
echo "  - Frontend: http://localhost:3000"
echo "  - Backend API: http://localhost:8080/api"
echo "  - MySQL: localhost:3306"
echo ""
echo "To view logs, run:"
echo "  $DOCKER_COMPOSE logs -f"
echo ""
echo "To stop the application, run:"
echo "  $DOCKER_COMPOSE down"
echo "==================================="
