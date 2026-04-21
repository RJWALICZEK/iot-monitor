# IoT Monitor – Mikroserwisowy system zbierania danych z ESP32

## 📌 Opis projektu

IoT Monitor to aplikacja oparta o architekturę mikroserwisową umożliwiająca zbieranie, przechowywanie i analizę danych telemetrycznych z urządzeń IoT (ESP32 z czujnikiem DHT11).

System składa się z:
- backendu w Spring Boot (REST API),
- bazy danych PostgreSQL + TimescaleDB,
- urządzenia IoT (ESP32),
- mechanizmu migracji bazy danych (Flyway).

Dane takie jak temperatura, wilgotność, czas pomiaru oraz lokalizacja urządzenia są wysyłane przez ESP32 do API i zapisywane w bazie danych w czasie rzeczywistym.

---

## 🏗️ Architektura systemu

ESP32 (DHT11) → HTTP REST → Spring Boot API (8080) → PostgreSQL + TimescaleDB

---

## ⚙️ Technologie

- Java 21+
- Spring Boot 3.5
- Spring Data JPA
- PostgreSQL 15
- TimescaleDB
- Flyway
- ESP32 (Arduino framework)
- DHT11
- REST API (JSON)

---

## 📡 REST API

### POST /api/v1/measurements

Przykładowy request:

    json
{
  "temperature": 23.5,
  "humidity": 45.0,
  "device": "ESP32_DevKit",
  "location": "serwerownia pokój 1.12"
}
GET /api/v1/measurements

Zwraca listę wszystkich pomiarów z bazy danych.

🗄️ Baza danych

Tabela: measurements

id (BIGSERIAL)
ts (TIMESTAMPTZ)
temperature (DOUBLE PRECISION)
humidity (DOUBLE PRECISION)
device (VARCHAR)
location (VARCHAR)

Dodatkowo:

TimescaleDB hypertable
indeks na ts DESC
🔌 ESP32 (IoT)

Urządzenie:

ESP32 DevKit
czujnik DHT11

Funkcje:

łączenie z WiFi
odczyt temperatury i wilgotności
wysyłanie danych co 10 sekund do API

Endpoint: http://<server-ip>:8080/api/v1/measurements

🧪 Status projektu
✔ ZREALIZOWANE
Spring Boot REST API (FR-001, FR-006, FR-005)
zapis danych do PostgreSQL (FR-002)
integracja ESP32 → API → baza danych
Flyway migracje
TimescaleDB hypertable
działający pipeline IoT → backend → DB
lokalne środowisko uruchomieniowe
🔄 W TRAKCIE
interfejs webowy (FR-003, FR-004)
aplikacja mobilna (FR-003, FR-009)
dashboard wizualizacji danych
testy jednostkowe i integracyjne (QR-002)
dokumentacja LaTeX (FR-007)
UML diagramy (FR-008)
model PCB ESP32 (FR-011)
bezpieczeństwo SSL/TLS (NFR-004)
CI/CD (QR-003)
🚀 Uruchomienie projektu
Backend
./mvnw spring-boot:run
Baza danych

PostgreSQL + TimescaleDB (baza: iotdb)

📊 Przepływ danych

ESP32 → JSON → REST API → Spring Boot → PostgreSQL → TimescaleDB

📌 Autor

Projekt edukacyjny – system IoT w architekturze mikroserwisowej
Spring Boot + ESP32 + PostgreSQL + TimescaleDB
