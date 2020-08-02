#  Отчет о тестировании веб-сервиса 
18.07.2020 - 28.07.2020 было проведено тестирование приложения "Путешествие дня"

В процессе тестирования было проведено автоматизированное тестирование позитивной и негативной проверки заполнения всех полей формы. Для этих целей был написан набор автотестов.
В систему автоматизации тестирования интегрированы отчёты Gradle и Allure.

Выполнено 17 автотестов, из них 8 (47%) выполнены успешно, 9 (53%) не успешно:
![Allure1.JPG](https://github.com/VeraOm/Diplom/blob/master/Allure1.JPG)

В результате тестирования выявлены следующие дефекты:
* [Оплата по заблокированной дебетовой карте одобрена банком #2](https://github.com/VeraOm/Diplom/issues/2)
* [Некорректное сообщение для пустого поля "Год" #3](https://github.com/VeraOm/Diplom/issues/3)
* [Одобрен кредит по заблокированной карте банка #4](https://github.com/VeraOm/Diplom/issues/4)
* [Некорректное сообщение для пустого поля "Месяц" #5](https://github.com/VeraOm/Diplom/issues/5)
* [Прохождение оплаты для невалидного имени держателя карты #6](https://github.com/VeraOm/Diplom/issues/6)
* [Не скрываются ошибки значений полей формы заказа после их исправления #7](https://github.com/VeraOm/Diplom/issues/7)
* [Некорректное сообщение для пустого поля "CVC/CVV" #8](https://github.com/VeraOm/Diplom/issues/8)
* [Некорректное сообщение для пустого поля "Номер карты" #9](https://github.com/VeraOm/Diplom/issues/9)
* [Неверная связь в базе данных при оформлении кредита #10](https://github.com/VeraOm/Diplom/issues/10)

![Allure2.JPG](https://github.com/VeraOm/Diplom/blob/master/Allure2.JPG)

## Общие рекомендации

Обратить внимание на следующие основные моменты где были выявлены дефекты:
 - отображение и текст сообщений полей формы ввода;
 - отсутствие обработки действий по заблокированным картам;
 - неправильную связь в базе данных при покупке в кредит.

## Описание процесса тестирования

В процессе тестирования использовались следующие артефакты:
* [Проект](https://github.com/VeraOm/Diplom)
* [Проверяемый файл программы](https://github.com/VeraOm/Diplom/blob/master/aqa-shop.jar)
* [Симулятор банковских сервисов](https://github.com/netology-code/qa-diploma/blob/master/gate-simulator)
* [Правила оформления баг-репортов](https://github.com/netology-code/javaqa-homeworks/blob/master/report-requirements.md)

В качестве тестовых данных использовались данные (https://github.com/netology-code/qa-diploma-README.md)


Тестирование производилось в следующем окружении:
* ОС: Windows x64 Home
* Java: openjdk version "11.0.6" 2020-01-14
* Docker: Docker Toolbox v19.03.1
* Node.js: Docker образ node:8.16.2-alpine
* MySQL: Docker образ mysql:8.0.19
* PostgeSQL: Docker образ postgres:12-alpine
