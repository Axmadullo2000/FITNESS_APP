# GymApp

https://fitnessapp-production-1452.up.railway.app
A gym management web application built with Spring Boot and Thymeleaf. Handles client registration, membership cards, locker assignment, and real-time check-in / check-out tracking.

---

## Features

- **Client management** — register clients with personal info and gender, search by name or phone
- **Memberships & cards** — 4 tariff types (daily / alternate × with trainer / without), 12 or 24 sessions per card, 30-day expiry
- **Check-in / Check-out** — assign a locker on entry, free it on exit, track who is in the gym right now
- **Locker management** — 100 lockers split into male (1–50) and female (51–100) sections, visual grid showing free / occupied status
- **Dashboard** — live stats: total clients, visitors in gym, free and occupied lockers

---

## Tech Stack

| Layer | Technology |
|---|---|
| Backend | Spring Boot 3.2, Spring MVC, Spring Data JPA |
| Frontend | Thymeleaf, Bootstrap 5, Bootstrap Icons |
| Database | H2 (file-based, persisted) |
| Build | Maven |
| Java | 17 |

---

## Getting Started

### Prerequisites

- Java 17+
- Maven 3.8+

### Run

```bash
git clone <repo-url>
cd gym-app
mvn spring-boot:run
```

Open [http://localhost:8080](http://localhost:8080) in your browser.

### Database

The app uses an H2 file database stored at `./data/gymdb`. The schema is created automatically on first run via `spring.jpa.hibernate.ddl-auto=update`.

To inspect the database directly, open the H2 console at [http://localhost:8080/h2-console](http://localhost:8080/h2-console):

| Field | Value |
|---|---|
| JDBC URL | `jdbc:h2:file:./data/gymdb` |
| Username | `sa` |
| Password | _(empty)_ |

---

## Seed Data

On startup, `DataInitializer` automatically creates:

**4 tariffs**

| Name | Sessions | Type |
|---|---|---|
| Ежедневно с тренером | 12 | EVERY_DAY_WITH_TRAINER |
| Ежедневно без тренера | 12 | EVERY_DAY_NO_TRAINER |
| Через день с тренером | 24 | ALTERNATE_WITH_TRAINER |
| Через день без тренера | 24 | ALTERNATE_NO_TRAINER |

**100 lockers** (1–50 male, 51–100 female), all FREE on first run

---

## Project Structure

```
src/main/java/com/gym/
├── config/
│   └── DataInitializer.java       # Seeds tariffs and lockers on startup
├── controller/
│   ├── DashboardController.java
│   ├── ClientController.java
│   ├── VisitController.java
│   └── LockerController.java
├── entity/
│   ├── Client.java
│   ├── Tariff.java
│   ├── Membership.java
│   ├── Card.java
│   ├── Locker.java
│   ├── Visit.java
│   └── enums/  (CardStatus, MembershipStatus, LockerStatus, VisitStatus, TariffType, Gender)
├── repository/
│   ├── ClientRepository.java
│   ├── MembershipRepository.java
│   ├── CardRepository.java
│   ├── LockerRepository.java
│   ├── VisitRepository.java
│   └── TariffRepository.java
└── service/
    ├── ClientService.java
    ├── MembershipService.java
    ├── VisitService.java
    └── LockerService.java

src/main/resources/
├── templates/
│   ├── fragments/layout.html      # Shared navbar + sidebar layout
│   ├── dashboard.html
│   ├── clients/
│   │   ├── list.html
│   │   ├── form.html
│   │   └── detail.html
│   ├── visits/
│   │   └── checkin.html
│   └── lockers/
│       └── list.html
└── static/css/
    └── app.css                    # Custom design system
```

---

## Card Expiry Rules

A membership card expires when **either** condition is met:

- All sessions have been used (`remainingSessions == 0`)
- 30 days have passed since the card was issued (`now > expiresAt`)

Expiry is checked automatically on each check-in. When a card expires, both the card status and the membership status are set to `EXPIRED`.

---

## Pages

| URL | Description |
|---|---|
| `/` | Dashboard — live stats and current visitors |
| `/clients` | Client list with search |
| `/clients/new` | Register new client |
| `/clients/{id}` | Client detail — card, sessions, membership history |
| `/visits` | Check-in / Check-out |
| `/lockers` | Locker grid — male and female sections |
