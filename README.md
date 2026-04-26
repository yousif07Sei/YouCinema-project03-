# 🎬 YouCINEMA — Cinema Seat Booking REST API

A full-featured RESTful API for a cinema seat booking system built with Spring Boot 4, PostgreSQL, and Spring Security + JWT.

---

## 🛠 Tech Stack

| Technology | Version |
|---|---|
| Java | 17 |
| Spring Boot | 4.0.5 |
| PostgreSQL | 16 |
| Spring Security | 7.0.4 |
| JWT (jjwt) | 0.11.5 |
| Hibernate ORM | 7.2.7 |
| Lombok | 1.18.44 |
| Maven | - |

---

## ⚙️ Setup & Installation

### Prerequisites
- Java 17
- PostgreSQL (running on port 5433)
- Maven
- Docker (optional, for Mailpit)

### 1. Clone the repository
```bash
git clone https://github.com/your-username/YouCINEMA.git
cd YouCINEMA
```

### 2. Configure the database
Create a PostgreSQL database:
```sql
CREATE DATABASE youcinema_dev;
```

### 3. Configure application properties
Edit `src/main/resources/application-dev.properties`:
```properties
spring.datasource.url=jdbc:postgresql://localhost:5433/youcinema_dev
spring.datasource.username=your_username
spring.datasource.password=your_password

jwt.secret=your_jwt_secret
jwt.expiration=86400000

spring.mail.host=localhost
spring.mail.port=1025
```

### 4. Run Mailpit (local email testing)
```bash
docker run -d -p 8025:8025 -p 1025:1025 axllent/mailpit
```
Access Mailpit UI at: http://localhost:8025

### 5. Build and run
```bash
mvn clean install
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

The API will be available at: `http://localhost:8080`

---

## 🌱 Data Seeding

On startup, the app automatically seeds the database with:

| Seeder | Data |
|---|---|
| UserSeeder | 1 admin, 2 customers |
| MovieSeeder | 4 Fast & Furious movies |
| CinemaHallSeeder | 3 halls (Standard 50 seats, IMAX 40 seats, VIP 20 seats) |
| ShowtimeSeeder | 5 showtimes |

**Seeded credentials:**
```
admin@youcinema.com  / Admin1234!
john@youcinema.com   / John1234!
jane@youcinema.com   / Jane1234!
```

> Seeders use `count() > 0` check — they skip if data already exists.

---

## 🔐 Authentication

The API uses **JWT Bearer Token** authentication.

1. Register or login to receive a token
2. Include the token in all protected requests:
```
Authorization: Bearer <your_token>
```

---

## ❌ Error Handling

All errors return clean JSON responses via `GlobalExceptionHandler`:

| Status | Meaning |
|---|---|
| 400 | Bad Request — validation error |
| 403 | Forbidden — insufficient role |
| 404 | Not Found — resource does not exist |
| 409 | Conflict — seat already booked / resource already exists |

Example:
```json
{
    "error": "Seat A5 is already booked for this showtime"
}
```

---

## 🗂 Project Structure

```
com.ga.YouCINEMA/
├── controller/       # REST controllers
├── model/            # JPA entities
├── service/          # Business logic
├── repository/       # Spring Data JPA repositories
├── dto/
│   ├── request/      # Request DTOs
│   └── response/     # Response DTOs
├── enums/            # Enums (UserRole, SeatStatus, BookingStatus, etc.)
├── security/         # JWT filter, UserDetails, SecurityConfig
├── util/             # JwtUtils, EmailUtils
├── exception/        # Custom exceptions + GlobalExceptionHandler
└── seeder/           # Data seeders
```

---

## 🔒 Roles & Permissions

| Role | Permissions |
|---|---|
| `ROLE_ADMIN` | Full access — manage movies, halls, showtimes, users |
| `ROLE_CUSTOMER` | Browse movies/showtimes, book seats, manage own bookings |

---

## ⚡ Concurrency Handling

Booking uses a **dual-layer concurrency control** to prevent double-booking:

**Layer 1 — ReentrantLock (Application level)**
- One `ReentrantLock` per seat ID stored in a `ConcurrentHashMap`
- Threads acquiring the same seat are blocked until the current booking completes

**Layer 2 — Optimistic Locking (Database level)**
- `@Version` annotation on the `Seat` entity
- Catches any concurrent DB writes that slip through the application lock

```
Thread 1 → acquires lock for seat A1 → checks → AVAILABLE → books ✅
Thread 2 → BLOCKED → waits → acquires lock → ALREADY BOOKED → 409 ❌
```

---

## 📋 API Endpoints

See [ENDPOINTS.md](ENDPOINTS.md) for the full endpoint reference.

---

## 📧 Email Features

- **Email Verification** — sent on registration, expires in 24 hours
- **Password Reset** — token-based reset link, expires in 1 hour

---

## 🗄 Database Models

| Model | Key Fields |
|---|---|
| User | role, status (ACTIVE/INACTIVE), emailVerified, soft delete |
| Movie | title, genre, language, duration, releaseDate, posterUrl |
| CinemaHall | name, hallType (STANDARD/IMAX/VIP), totalSeats |
| Seat | row, seatNumber, seatType, status, @Version |
| Showtime | movie, cinemaHall, startTime, endTime, price |
| Booking | user, showtime, bookedSeats, totalPrice, status |
| BookingSeat | booking, seat (join table) |
| EmailVerificationToken | token, expires 24h |
| PasswordResetToken | token, expires 1h |

---

## 👤 Author

**Yousif**  
YouCINEMA 🎬