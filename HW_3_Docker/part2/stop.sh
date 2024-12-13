#!/bin/bash

echo "Остановка контейнеров и очистка..."

docker stop client > /dev/null
docker rm client > /dev/null
docker rmi deniszierpka/pddis_2024_hw-hw3-client:latest > /dev/null

docker stop server > /dev/null
docker rm server > /dev/null
docker rmi deniszierpka/pddis_2024_hw-hw3-server:latest > /dev/null

docker network rm tmp-network > /dev/null 2>&1

docker-compose down > /dev/null 2>&1

echo "Остановка завершена."