#!/bin/bash

echo "# Docker-compose down"
docker-compose -f docker-compose.yml down

echo "# Docker-kafka-compose down"
docker-compose -f docker-kafka-compose.yml down
