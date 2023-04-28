#!/bin/bash

working_directory="/mnt/c/Users/USER/Desktop/Study/Spring/Project/msa_example"
docker_username="eheh12321"
build_flag=false

while getopts ":b" opt; do
  case $opt in
  b)
    echo ">>> get build option"
    build_flag=true
    ;;
  \?)
    echo "[$OPTARG] is invalid option."
    exit 1
    ;;
  esac
done

if [ $build_flag = true ]; then
  echo "# Eureka_gateway Image Build..."
  cd ./eureka_gateway
  docker build -t ${docker_username}/eureka-gateway .
  echo "# Success!"
  sleep 1s

  echo "# Eureka_kafka_consumer Image Build..."
  cd ../eureka_kafka_consumer
  docker build -t ${docker_username}/eureka-kafka-consumer .
  echo "# Success!"
  sleep 1s

  echo "# Eureka_kafka_producer Image Build..."
  cd ../eureka_kafka_producer
  docker build -t ${docker_username}/eureka-kafka-producer .
  echo "# Success!"
  sleep 1s

  echo "# Eureka_server Image Build..."
  cd ../eureka_server
  docker build -t ${docker_username}/eureka-server .
  echo "# Success!"
  sleep 1s

  echo "# Eureka_tcp_server Image Build..."
  cd ../eureka_tcp_server
  docker build -t ${docker_username}/eureka-tcp-server .
  echo "# Success!"
  sleep 1s
fi

cd $working_directory

echo "# Docker-kafka-compose Up"
docker-compose -f docker-kafka-compose.yml up -d

echo "# Docker-compose Up"
docker-compose -f docker-compose.yml up -d
