#!/bin/bash

set -e 

echo "Вызов stop.sh для консистентности"
./stop.sh
rm -rf ./app

echo "Создание сети..."
docker network create tmp-network > /dev/null

echo "Сборка Docker образов..."
docker build -f Dockerfile -t builder .
docker build -f Dockerfile -t nginx .

echo "Запуск контейнеров..."
docker-compose up -d builder
docker-compose up -d nginx

echo "Проверка соединения Ansible..."
docker run --rm builder ansible -i /ansible/inventories/servers.yml -m ping all --limit builder
docker run --rm nginx ansible -i /ansible/inventories/servers.yml -m ping all --limit nginx


docker exec -it builder sh -c "ansible-playbook -i /ansible/inventories/servers.yml /ansible/playbooks/build_app.yml --limit builder"

docker exec -it builder sh -c "ansible-playbook -i /ansible/inventories/servers.yml /ansible/playbooks/run_app.yml --limit builder"

docker exec -it nginx sh -c "ansible-playbook -i /ansible/inventories/servers.yml /ansible/playbooks/setup_nginx.yml --limit nginx"

echo "Запуск завершен!"
echo "------------------- Информация ------------------------"
echo "Проверка работы приложения - http://127.0.0.1:5000"
echo "Проверка работы nginx - http://127.0.0.1:80"
echo "Получение страницы приложения через nginx - http://127.0.0.1:80/app"
echo "-------------------------------------------------------"
