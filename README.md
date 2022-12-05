# GWT Application

## About project
Данный проект является тестовым заданием. Он представляет собой
приложение для переброса записей между двумя списками: линейным
и древовидным. Также есть функциисохранения данных в базу данных
mongoDB и их загрузки.

## Technology stack
Технологии проекта:
- Приложение написано на языке Java.
- Используются библиотеки для преобразования Java в JavaScript
на клиентской части - GWT/GXT.
- Серверная часть написана на Spring Framework.
- Связь между серверной и клиентской частями выполнена при помощи
библиотеки RestyGWT.

## How to launch the Project
Запуск приложения производится из среды разработки IntelliJ Idea.

Для использования функций сохранения и загрузки требуется подключить
базу данных mongoDB. В своём проекте я подключал базу данных через
докер-контейнер skillbox. Для запуска БД достаточно запустить докер и
ввести в cmd данную команду:

```
docker run --rm --name skill-mongo -p 127.0.0.1:27017:27017/tcp -d scalar4eg/skill-mongo-with-hacker
```
