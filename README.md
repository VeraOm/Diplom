## Настройка запуска авто-тестов

До запуска автотестов необходимо перейти в папку с установлеными тестами и выполнить следующие шаги:
1. Установить и запустить Docker (Docker Toolbox) и Docker-compose.
1. Запустить в Docker контейнеры СУБД MySQl и PostgerSQL.
1. Запустить в Docker контейнер Node.js
1. Откорректировать настройку тестируемого приложения для использования СУБД и Node.js.
1. Запустить тестируемое приложение

### Установка и запуск MySQL в качестве Docker-контейнера
1. В Docker перейти в папку `./mysql`
1. Выполнить команду `docker-compose up -d`
1. Дождаться сообщения о готовности MySQL

### Установка и запуск PostgerSQL в качестве Docker-контейнера
1. В Docker перейти в папку `./postgresql`
1. Выполнить команду `docker-compose up -d`
1. Дождаться сообщения о готовности PostgerSQL

### Установка и запуск Node.js в качестве Docker-контейнера
1. В Docker перейти в папку `./gate-simulator`
1. Выполнить команду `docker-compose up -d`
1. Дождаться сообщения о готовности Node.js

### Настройка тестируемого приложения
Необходимо отредактировать файл `application.properties`:
 - Вписать IP адрес или URL по которому доступен Docker в параметрах: `spring.credit-gate.url`, `spring.payment-gate.url`, `spring.datasource.url`.
 - При прохождении тестов на MySQL в параметре `spring.datasource.url` необходимо написать `jdbc:mysql:` и порт 3306.
 - При прохождении тестов на PostgerSQL в параметре `spring.datasource.url` необходимо написать `jdbc:postgresql:` и порт 5432.

### Запуск тестируемого приложения
Для запуска тестируемого приложения необходимо выполнить команду `java -jar aqa-shop.jar`.

## Выполнение авто-тестов

Выполнение авто-тестов запускается файлом `gradlew.bat clean build` (в Windows) или `gradlew clean build` (в Linux).

Для получения отчета Allure необходимо сначала выполнить команду `gradlew allureReport`, для подготовки Allure. Затем запустить автотесты командой `gradlew clean test allureReport`. Для просмотра отчета Allure необходимо выполнить команду `gradlew allureServe` и дождаться открытия отчета в браузере.

