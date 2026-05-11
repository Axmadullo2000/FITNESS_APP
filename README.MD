# GymApp — Fitness Club Management System

A web-based management system for a fitness club built with Spring Boot. Covers the full client lifecycle: registration, membership assignment, locker management, visit tracking, and QR-based check-in/check-out via tablet scanner.

Live demo: https://fitnessapp-production-1452.up.railway.app

---

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java 17, Spring Boot 3.5 |
| Security | Spring Security 6, role-based (ADMIN / CLIENT) |
| Persistence | Spring Data JPA, Hibernate, PostgreSQL |
| Frontend | Thymeleaf 3, custom CSS design system |
| QR Codes | ZXing 3.5 (generation), html5-qrcode (scanning) |
| Build | Maven |
| Containerization | Docker (multi-stage build) |

---

## Features

### Admin panel
- **Dashboard** — live stats: active clients, open visits, free/occupied lockers
- **Client management** — create and view client profiles, search by name or phone
- **Memberships** — assign tariffs, track start/end dates and remaining sessions
- **Manual check-in / check-out** — locker assignment on entry, release on exit
- **Locker map** — visual grid (100 lockers, split male/female), color-coded by status
- **QR scanner** — tablet-friendly page for self-service entry; client shows QR → system opens the visit automatically

### Client cabinet
- Personal page showing active membership, tariff details, and expiry date
- **QR card** — full-screen QR code for showing at the entrance scanner

---

## Domain Model

```
AppUser     — system account (ADMIN / CLIENT role), phone-based login
Client      — gym member profile (name, phone, gender, birthday)
Tariff      — pricing plan (type, price, duration in days)
MemberShip  — links a client to a tariff, tracks start/end dates and status
Card        — scannable QR card tied to a membership (ACTIVE → IN_USE → EXPIRED)
Visit       — single gym entry (OPEN while inside, CLOSED on exit)
Locker      — physical locker 1–100 (FREE / OCCUPIED), assigned during check-in
```

**Tariff types**

| Type | Description |
|---|---|
| `EVERY_DAY_WITH_TRAINER` | Every day + trainer |
| `EVERY_DAY_NO_TRAINER` | Every day, no trainer |
| `EVEY_OTHER_DAY_WITH_TRAINER` | 3 times per week + trainer |
| `EVEY_OTHER_DAY_NO_TRAINER` | 3 times per week, no trainer |

---

## Getting Started

### Prerequisites

- Java 17+
- PostgreSQL 14+
- Maven 3.8+

### 1. Create the database

```sql
CREATE DATABASE fitness_app;
```

### 2. Configure credentials

Edit `src/main/resources/application-dev.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/fitness_app
spring.datasource.username=your_username
spring.datasource.password=your_password
```

### 3. Run

```bash
./mvnw spring-boot:run
```

The app starts at `http://localhost:8080`.

On first run, `DataInitializer` seeds the database with default tariffs, 100 lockers (1–50 male / 51–100 female), and a default admin account.

**Default admin credentials:**
```
Phone:    +998 90 000 00 00
Password: admin123
```

---

## Running with Docker

```bash
docker build -t gymapp .

docker run -p 8080:8080 \
  -e DATABASE_URL=jdbc:postgresql://host:5432/fitness_app \
  -e PGUSER=postgres \
  -e PGPASSWORD=secret \
  gymapp
```

---

## URL Map

| URL | Role | Description |
|---|---|---|
| `/login` | Public | Login page (phone + password) |
| `/register` | Public | Client self-registration |
| `/dashboard` | Admin | Stats overview |
| `/clients` | Admin | Client list with search |
| `/clients/{id}` | Admin | Client detail, membership, QR |
| `/lockers` | Admin | Locker map |
| `/visits/checkin/{id}` | Admin | Manual check-in |
| `/visits/checkout/{id}` | Admin | Manual check-out |
| `/scan` | Admin | QR scanner (tablet) |
| `/my/cabinet` | Client | Personal cabinet |
| `/my/card` | Client | QR card full-screen |

---

## Project Structure

```
src/main/java/com/gym/
├── config/          # SecurityConfig, DataInitializer, WebConfig
├── controller/      # MVC controllers (Auth, Client, Dashboard, Locker, QR, Scan, Visit)
├── entity/          # JPA entities
│   └── types/       # Enums: CardStatus, VisitStatus, LockerStatus, TariffType, Gender
├── repository/      # Spring Data JPA repositories
├── service/         # Service interfaces
│   └── impl/        # Implementations
└── util/            # PhoneUtils — normalizes phone numbers before save/login

src/main/resources/
├── templates/       # Thymeleaf HTML templates
└── static/css/      # app.css, auth.css, cabinet.css, card.css, scanner.css
```

---

## Profiles

| Profile | Purpose |
|---|---|
| `dev` (default) | Local PostgreSQL, credentials in `application-dev.properties` |
| `prod` | Reads `DATABASE_URL`, `PGUSER`, `PGPASSWORD` from environment variables |

Switch in `application.properties`:
```properties
spring.profiles.active=prod
```

---

## Card & Visit Logic

- A card is created `ACTIVE` when a membership is assigned.
- On QR scan or manual check-in: card moves to `IN_USE`, a `Visit` record opens (`OPEN`), a locker is assigned.
- On check-out: visit closes (`CLOSED`), locker is freed, card returns to `ACTIVE` (or `EXPIRED` if sessions/days are exhausted).
- A card expires when all sessions are used **or** the membership period ends.

---

## MVP Limitations

- No email or SMS notifications
- No payment processing integration
- No multi-branch support
- Tariff prices are set at initialization; no in-app editor
- No mobile app — the UI is responsive but browser-based

---

## Documentation

- [`GymApp_Documentation.docx`](GymApp_Documentation.docx) — full technical documentation in Russian
- [`GymApp_Guide.pptx`](GymApp_Guide.pptx) — non-technical user guide for admins and clients (Russian, 20 slides)
