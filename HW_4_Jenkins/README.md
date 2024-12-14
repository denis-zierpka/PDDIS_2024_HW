## Домашнее задание №4 

### **Тема:** Jenkins

---

### **Постановка задачи:**
```
Взять приложение из п.3 (или любое другое), автоматизировать его сборку в Jenkins (pipeline и freestyle Job) на событие pull-request/push.
В pipeline должны входить:
- сборка приложения (maven, другой сборщик)
- запуск автотестов (unit в зависимости от проекта, postman)
- сборка результатов работы тестов в allure и отброска в Jenkins
- анализ исходного кода Sonar (в том числе необходимо исправить все ошибки и (добиться не менее 90% покрытия кода тестами)* зависит от проекта)
- деплой приложения через Ansible (из лаб №2) или сборка контейнера (т.е. отказ от ансибл)
```

---

### **Описание решения / подтверждения:**


Начинаем работу с запуска `docker-compose up -d`

Мы получаем запущенные на `http://localhost:8080/` и `http://localhost:9000/` соответственно `Jenkins` и `SonarQube`

После чего приступаем к их настройке (произведенные настройки в конце - в блоке `Подробнее про настройки`)


#### Полученные результаты allure:
(все тесты пройдены)

![](resources/res-allure.png)

#### Полученные результаты sonar:
(необходимые 90% достигнуты)

![](resources/res-sonar.png)


#### Основной экран выглядит так:
![](resources/Jenkins-main.png)


#### Автоматизация сборки через webhooks:
![](resources/github-webhook.png)

P.s. для настройки использовал ngrok (`ngrok http 8080`)


#### Docker registry:
![](resources/registry.png)


#### Итоги:

- Автоматизированная сборка в Jenkins по событию
- Сборка с использованием maven
- Запуск тестов
- Получение и сохранение результатов allure
- Анализ и результаты sonar
- Сборка и отправка в docker registry

---

### **Подробнее про настройки:**

#### Устанавливаем необходимый плагины в Jenkins (Allure, Sonarqube, Docker)
![](resources/plugins.png)

#### В tools настраиваем конкретные версии:
![](resources/tools-git.png)

![](resources/tools-maven.png)

![](resources/tools-allure.png)

![](resources/tools-sonar.png)

![](resources/tools-docker.png)


#### В System прописываем нужные конфигурации:

![](resources/conf-sonar.png)

P.s. обращаем внимание на то, что доступ будет из контейнера производиться, поэтому указываем для SonarQube ip из ifconfig

#### Настраиваем токен в SonarQube:
(Тот, который должны вписать в credentials Jenkins (далее))

![](resources/creds-sonar.png)


#### Настраиваем credentials в Jenkins:
(Добавляем сюда тот самый токен из SonarQube - `'Java-token'` и данные для входа в docker registry - `docker-credentials-id`)

![](resources/creds-jenkins.png)


#### Далее создаем новый проект - pipeline
(Это собственно та самая джоба, которую планируем запускать)

Настраиваем возможность использовать некоторые события для автоматического запуска (webhooks)

![](resources/hooks.png)

Последняя и самая важная настрока - указываем ссылку на github, где лежат файлы проекта, указываем ветку и Jenkinsfile

![](resources/branch.png)


На этом настройки завершены, результаты работы - выше