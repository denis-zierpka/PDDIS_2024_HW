## Домашнее задание №3 

### **Тема:** Docker

---

### **Постановка задачи:**
```
Задание состоит из двух частей:
1. Через dockerfile собрать свое рабочее приложение и отправить его в docker-registry
2. Собрать через docker-compose двух или более компонентное приложение (состоящее из более чем одного docker image), где один компонент БД и научить их ""общаться"" между собой.
```

---

### **Использование и описание решения:**

#### 1. Собираем приложения и отправляем в docker-registry

В рамках первой части задания были собраны и отправлены в `docker-registry` две части приложения (`server` и `client`) 

! Все последующие действия этого пунка происходят в директории `part1`

Выполненяем следующие команды:

```
docker network create tmp-network > /dev/null
docker build -f client_app/Dockerfile -t client client_app
docker build -f server_app/Dockerfile -t server server_app
docker-compose up -d 
```

Далее отправляем в docker registy:
```
docker login
docker tag client:latest deniszierpka/pddis_2024_hw-hw3-client:latest
docker push deniszierpka/pddis_2024_hw-hw3-client:latest 
docker tag server:latest deniszierpka/pddis_2024_hw-hw3-server:latest
docker push deniszierpka/pddis_2024_hw-hw3-server:latest 
```

Найти их можно по ссылкам: [server](https://hub.docker.com/r/deniszierpka/pddis_2024_hw-hw3-server) и [client](https://hub.docker.com/r/deniszierpka/pddis_2024_hw-hw3-client)

---

#### 2. Собираем двухкомпонентное приложение

Используем загруженные ранее в docker-registry части приложения (`server` и `client`)

! Все последующие действия этого пунка происходят в директории `part2`

Для запуска достаточно выполнить команду: `./start.sh`

Остановить и произвести очистку созданных контейнеров и образов можно командой: `./stop.sh`

В рамка скрипта `start.sh` будут скачены и запущены необходимые для работы контейнеры.

### Комментарий:

В рамках примера использовано следующее приложение:

На клиенте происходит получение от пользователя некоторого числа, которое отдельным запросом к серверу возводится в квадрат, результат возвращается пользователю


После запуска можно проверить работу приложения запросив разультат, например, для числа `10`: http://127.0.0.1:8000/client?number=10 

Должны получить результат: `100`