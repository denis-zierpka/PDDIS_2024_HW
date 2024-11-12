#!/bin/bash

set -e 

echo "Вызов stop.sh для консистентности"
./stop.sh

echo "Создание сети..."
docker network create tmp-network > /dev/null

echo "Запуск контейнеров из Docker Registry..."
docker-compose up -d 

echo "Запуск завершен!"
echo "------------------- Информация ------------------------"
echo "Проверка работы - http://127.0.0.1:8000/client?number=10"
echo "-------------------------------------------------------"