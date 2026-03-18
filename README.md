# Task Manager Demo

Приложение для демонстрации RESTful-сервиса для управления задачами с разделением ролей (USER, ADMIN) и авторизацией через **JWT** с использованием Spring Boot, обеспечивающий базовый функционал создания, редактирования, назначения и фильтрации задач.

## Описание проекта
Проект реализует два контроллера для реализации функционала. 
- **AuthController** - отвечает за регистрацию пользователя в системе и его авторизацию (вхождение в систмеу)
- **TaskController** - отвечает за работу с пользовательскими задачами (создание, редактирование, удаление и получения списка всех задач).

## Технологический стек
- **Java 17**.
- **Spring Boot 3.4.3**.
- **Spring Security** + **JWT**.
- **Spring Data JPA** + **PostgreSQL/H2**.
- **MapStruct**: Маппинг сущностей в DTO на этапе компиляции.
- **JPA Specifications**: Динамический поиск по сложным фильтрам.
- **Swagger/OpenAPI 3.0** (документация).
- **JUnit 5**, **Mockito**, **AssertJ** (тестирование).
- **Docker** & **Docker Compose**

## Быстрый старт

### 1. Требования 
*   Docker & Docker Compose
*   Maven 3.9+ & Java 17 (для локальной сборки)

### 2. Структура проекта
* src/main/java/.../config — настройки Security, JWT и OpenAPI.
* src/main/java/.../security — логика авторизации и проверки прав (TaskSecurity).
* src/main/java/.../dto
* src/main/java/.../mapper
* src/main/java/.../repository/model — Entity сущности (User, Task).
* src/main/resources/db/changelog — скрипты миграций Liquibase.
* src/main/resources/static/openapi.yml — описание API.

### 3. Инфраструктура
Инфраструктура описанная в *docker-compose.yaml* включает в себя:
- **PostgreSql**
- **task-manager-demo app** само приложение

**Сервис API: http://localhost:8080**

**PostgreSQL: localhost:5432 (user: admin, pass: secret)**

### 4. Запуск приложения
Билд и запуск приложения осуществляется командой
```bash
docker compose up --build -d
```
Это запустит базу PostgreSQL, выполнит миграции Liquibase и поднимет само приложение на порту 8080.
#### Локальный запуск без развертывания в docker
Команда сборки проекта
```bash
mvn clean package
```
В результате будет создан .jar файл (target/task_manager_demo-0.0.1-SNAPSHOT.jar)
Запуск приложения с установкой переменных окружения
#### Требование для локального запуска:
* Установленный и настроенный PostgreSQL 18.1
```bash
java -Dapplication.security.jwt.secret-key=ZmF6ZWRldi1zZWNyZXQta2V5LWZvci1qd3QtYXV0aGVudGljYXRpb24tMjAyNA==  -Dspring.datasource.url=jdbc:postgresql://localhost:5432/tasksdb  -Dspring.datasource.username=admin     -Dspring.datasource.password=secret  -jar target/task_manager_demo-0.0.1-SNAPSHOT.jar
```
* *secret-key* - секретный ключ необходимый для генерации JWT
* *datasource.url* - подключение к базе данных. Включает тип БД, хост, порт и имя схемы. По умолчанию jdbc:postgresql://localhost:5432/tasksdb
* *datasource.username* - пользователь базы данных, по умолчанию *admin*
* *password=secret* - пароль пользователя, по умолчанию *secret*
### 5. Документация API
После запуска Swagger UI доступен по адресу:
http://localhost:8080/swagger-ui/index.html


## Тестирование API
Тестовые сценарии (cURL)
1. Регистрация нового пользователя
```bash
   curl --location 'http://localhost:8080/api/auth/register' \
   --header 'Content-Type: application/json' \
   --data-raw '{
   "username": "radmiy",
   "email": "radmiy@example.com",
   "password": "password123",
   "role": "USER"
   }'
```
Ответ:
```bash
User registered successfully
```
1.2 Регистрация повторная пользователя
```bash
   curl --location 'http://localhost:8080/api/auth/register' \
   --header 'Content-Type: application/json' \
   --data-raw '{
   "username": "radmiy",
   "email": "radmiy@example.com",
   "password": "password123",
   "role": "USER"
   }'
```
Ответ:
```bash
{"message":"User with username: radmiy exists"}
```

### 2. Авторизация (Получение токена)
#### 2.1 Авторизация по *username*
```bash
   curl --location 'http://localhost:8080/api/auth/login' \
   --header 'Content-Type: application/json' \
   --data '{
    "username": "radmiy",
    "password": "password123"
   }'
```
Ответ:
```bash
{eyJhbGciOiJIUzI1NiJ9...}
```
Авторизация прошла успешно
#### 2.2 Авторизация по *email*
```bash
   curl --location 'http://localhost:8080/api/auth/login' \
   --header 'Content-Type: application/json' \
   --data '{
    "email": "radmiy@example.com",
    "password": "password123"
   }'
```
Ответ:
```bash
{eyJhbGciOiJIUzI1NiJ9...}
```
Авторизация прошла успешно
#### 2.3 Авторизация с некорректными кредами
```bash
   curl --location 'http://localhost:8080/api/auth/login' \
   --header 'Content-Type: application/json' \
   --data '{
    "username": "radm",
    "password": "password123"
   }'
```
Ответ:
```bash
{"message": "User with username: radm does not exist"}
```
Авторизация упала, так как нет такого пользователя в системе
### Следующая часть тестового прогона осуществляется с использованием полученного *JWT* в тестовом прогоне *Авторизация*
### 3. Создание задачи (Нужен Bearer Token)
```bash
   curl --location 'http://localhost:8080/api/tasks' \
   --header 'Content-Type: application/json' \
   --header 'Authorization: Bearer <TOKEN>' \
   --data '{
    "title": "Fix production bug",
    "description": "Analyze logs and fix NPE",
    "status": "TODO",
    "priority": "HIGH"
   }'
```
Ответ
```bash
{
    "id": "8cd55214-e0ad-4791-97a5-040a76ec0e9f",
    "title": "Fix production bug",
    "description": "Analyze logs and fix NPE",
    "status": "TODO",
    "priority": "HIGH",
    "author": {
        "id": "22199db8-ffe5-4ff7-a6b1-f4b06abf9d06",
        "username": "radmiy",
        "email": "radmiy@example.com"
    },
    "assignee": null,
    "createdAt": "2026-03-17T13:17:49.515845Z",
    "updatedAt": "2026-03-17T13:17:49.515879Z"
}
```
Любой авторизированный пользователь может создавать задачи, при этом в поле *автор* будет записан авторизированный пользователь, полученный системой из контекста запроса. Так же в базу данных запишуться автосоздаваемые поля *createdAt* и *updatedAt*. 
### 4. Поиск задач с фильтрацией
```bash
   curl --location 'http://localhost:8080/api/tasks?status=TODO&author=3ed395a8-991a-4445-8103-29dc0420a738' \
   --header 'Content-Type: application/json' \
   --header 'Authorization: Bearer <TOKEN>' \
   --data ''
```
Ответ:
```bash
[
    {
        "id": "ac26aac7-13e9-490f-80c3-13341c39627e",
        "title": "Fix production bug",
        "description": "Analyze logs and fix NPE",
        "status": "TODO",
        "priority": "HIGH",
        "author": {
            "id": "3ed395a8-991a-4445-8103-29dc0420a738",
            "username": "radmiy",
            "email": "radmiy@example.com"
        },
        "assignee": null,
        "createdAt": "2026-03-17T13:28:00.517617Z",
        "updatedAt": "2026-03-17T13:28:00.517617Z"
    }
]
```
Авторизированный пользователь может читать задачи других пользователей, но не иметь возможность их редактирования и удаления. Исключение составляет только пользователь с ролью *ADMIN*, он может удалять и изменять чужие задачи.
### 5. Изменение задачи
#### 5.1. Залогинившийся пользователь изменяет свою задачу
```bash
    curl --location --request PUT 'http://localhost:8080/api/tasks/ac26aac7-13e9-490f-80c3-13341c39627e' \
    --header 'Content-Type: application/json' \
    --header 'Authorization: Bearer <TOKEN>' \
    --data '{
       "title": "Fix production bug",
       "description": "Analyze logs and fix NPE",
       "status": "IN_PROGRESS",
       "priority": "HIGH",
       "author": "3ed395a8-991a-4445-8103-29dc0420a738",
       "assignee": "3ed395a8-991a-4445-8103-29dc0420a738"
    }'
```
Ответ:
```bash
{
    "id": "ac26aac7-13e9-490f-80c3-13341c39627e",
    "title": "Fix production bug",
    "description": "Analyze logs and fix NPE",
    "status": "IN_PROGRESS",
    "priority": "HIGH",
    "author": {
        "id": "3ed395a8-991a-4445-8103-29dc0420a738",
        "username": "radmiy",
        "email": "radmiy@example.com"
    },
    "assignee": {
        "id": "3ed395a8-991a-4445-8103-29dc0420a738",
        "username": "radmiy",
        "email": "radmiy@example.com"
    },
    "createdAt": "2026-03-17T13:28:00.517617Z",
    "updatedAt": "2026-03-17T16:46:39.909598+03:00"
}
```
Авторизированный пользователь имеет возможномть изменять свою задачу.
#### 5.2. Залогинившийся пользователь изменяет чужую задачу
```bash
    curl --location --request PUT 'http://localhost:8080/api/tasks/4e3ce1a5-ffb2-4568-bb4d-6bd8eb401195' \
    --header 'Content-Type: application/json' \
    --header 'Authorization: Bearer <TOKEN>' \
    --data '{
       "title": "Fix production bug",
       "description": "Analyze logs and fix NPE",
       "status": "IN_PROGRESS",
       "priority": "HIGH",
       "author": "3ed395a8-991a-4445-8103-29dc0420a738",
       "assignee": "3ed395a8-991a-4445-8103-29dc0420a738"
   }'
```
Ответ:
```bash
{
    "message": "Access Denied"
}
```
Доступ запрещен для действий пользователя над чужими задачами
### 6. Удаление задачи
```bash
curl --location --request DELETE 'http://localhost:8080/api/tasks/ac26aac7-13e9-490f-80c3-13341c39627e' \
--header 'Content-Type: application/json' \
--header 'Authorization: Bearer <TOKEN>'
```
Ответ:
```bash
   204 No Content
```
Авторизированный пользователь может удалять свою задачу.
