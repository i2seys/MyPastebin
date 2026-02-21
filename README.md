# Pastebin

Распределённый веб-сервис для создания и хранения текстовых заметок (pastebin) с микросервисной архитектурой.

## 📋 Описание

Приложение позволяет пользователям создавать текстовые заметки (pastes) и получать их по уникальному хешу. Архитектура построена на взаимодействии двух Java-микросервисов через Apache Kafka, что обеспечивает асинхронную обработку запросов и масштабируемость.

### Модули

| Модуль | Описание | Порт | Технологии                           |
|--------|----------|------|--------------------------------------|
| **pastebinMicroservice** | Принимает запросы на создание и получение paste. Взаимодействует с hashMicroservice через Kafka. | 8090 | Java, Spring Boot, Spring Kafka      |
| **hashMicroservice** | Генерирует уникальные хеши для paste. Хранит хеши в базе данных. | 8091 | Java, Spring Boot, Spring Data JPA, Spring Kafka |
| **front** | Веб-интерфейс для работы с сервисом. Проксирует запросы к pastebinMicroservice. | 3000 | Node.js, Express, Vanilla JS         |
| **database** | Скрипты инициализации базы данных (init.sql). | — | PostgreSQL                           |

---

## 🛠️ Технологический стек

| Категория | Технологии |
|-----------|------------|
| **Backend** | Java 17, Spring Boot 3.x, Spring Web, Spring Data JPA, Spring Kafka |
| **Frontend** | Node.js, Express, Vanilla JavaScript, HTML/CSS |
| **Базы данных** | PostgreSQL 18 |
| **Брокер сообщений** | Apache Kafka 4.2.0 (KRaft mode) |
| **Сборка** | Maven |
| **Контейнеризация** | Docker, Docker Compose |

---

## 📁 Структура проекта

```
Pastebin/
├── docker-compose.yaml          # Оркестрация всех сервисов
├── .gitignore                   # Правила игнорирования файлов
├── README.md                    # Документация
│
├── database/
│   └── init.sql                 # Скрипт инициализации PostgreSQL
│
├── pastebinMicroservice/
│   ├── Dockerfile               # Сборка Java-приложения
│   ├── pom.xml                  # Maven-зависимости
│   ├── src/main/java/           # Исходный код
│   └── src/main/resources/
│       └── application.properties
│
├── hashMicroservice/
│   ├── Dockerfile               # Сборка Java-приложения
│   ├── pom.xml                  # Maven-зависимости
│   ├── src/main/java/           # Исходный код
│   └── src/main/resources/
│       └── application.properties
│
└── front/
    ├── Dockerfile               # Сборка Node.js-приложения
    ├── package.json             # NPM-зависимости
    ├── server.js                # Express-сервер
    └── public/
        └── index.html           # Веб-интерфейс
```

---

## 🚀 Быстрый старт

### Требования

- Docker
- Docker Compose

### Запуск приложения

1. **Клонируйте репозиторий:**
   ```bash
   git clone <repository-url>
   cd Pastebin
   ```

2. **Запустите все сервисы:**
   ```bash
   docker-compose up --build
   ```

   Или в фоновом режиме:
   ```bash
   docker-compose up -d --build
   ```

3. **Откройте приложение в браузере:**
   ```
   http://localhost:3000
   ```

### Остановка приложения

```bash
# Остановить контейнеры
docker-compose down

# Остановить и удалить тома (база данных будет очищена)
docker-compose down -v
```

## 📝 Примечания

1. **Инициализация базы данных** происходит только при первом запуске. При изменении `init.sql` необходимо удалить том:
   ```bash
   docker-compose down -v
   ```

2. **Микросервисы общаются через Kafka** асинхронно, что позволяет масштабировать каждый сервис независимо.

