#!/bin/bash

# EduManager System - Stop Script
# This script stops all running containers

echo "Stopping EduManager System..."

# Check if docker-compose exists (v1) or use docker compose (v2)
if command -v docker-compose &> /dev/null; then
    DOCKER_COMPOSE="docker-compose"
elif docker compose version &> /dev/null; then
    DOCKER_COMPOSE="docker compose"
else
    echo "Error: Neither 'docker-compose' nor 'docker compose' is available"
    exit 1
fi

$DOCKER_COMPOSE down

echo "EduManager System stopped successfully."
