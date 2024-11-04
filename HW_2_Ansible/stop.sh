#!/bin/bash

echo "Остановка контейнеров и очистка..."

docker stop builder > /dev/null
docker rm builder > /dev/null
docker rmi builder > /dev/null

docker stop nginx > /dev/null
docker rm nginx > /dev/null
docker rmi nginx > /dev/null

docker network rm tmp-network > /dev/null 2>&1

docker-compose down > /dev/null 2>&1

echo "Остановка завершена."
