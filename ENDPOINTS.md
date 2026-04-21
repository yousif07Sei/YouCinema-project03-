# 📋 YouCINEMA — API Endpoint Reference

Base URL: `http://localhost:8080`

---

## 🔓 Auth — Public

| Method | Endpoint | Description | Body |
|---|---|---|---|
| POST | `/auth/register` | Register new user | `firstName, lastName, email, password` |
| POST | `/auth/login` | Login and get JWT token | `email, password` |
| GET | `/auth/verify-email?token=` | Verify email address | — |
| POST | `/auth/forgot-password` | Send password reset email | `email` |
| POST | `/auth/reset-password?token=` | Reset password | `newPassword` |

---

## 👤 User — Protected

| Method | Endpoint | Role | Description |
|---|---|---|---|
| GET | `/api/users` | ADMIN | Get all users |
| GET | `/api/users/{id}` | ADMIN | Get user by ID |
| DELETE | `/api/users/{id}` | ADMIN | Soft delete user |
| GET | `/api/users/me` | CUSTOMER / ADMIN | Get own profile |
| PUT | `/api/users/me` | CUSTOMER / ADMIN | Update own profile |
| PUT | `/api/users/me/password` | CUSTOMER / ADMIN | Change own password |
| POST | `/api/users/me/profile-picture` | CUSTOMER / ADMIN | Upload profile picture |

---

## 🎬 Movie — Mixed

| Method | Endpoint | Role | Description |
|---|---|---|---|
| GET | `/api/movies` | PUBLIC | Get all movies |
| GET | `/api/movies/{id}` | PUBLIC | Get movie by ID |
| POST | `/api/movies` | ADMIN | Create movie |
| PUT | `/api/movies/{id}` | ADMIN | Update movie |
| DELETE | `/api/movies/{id}` | ADMIN | Delete movie |
| POST | `/api/movies/{id}/poster` | ADMIN | Upload movie poster |

---

## 🏛 Cinema Hall — Mixed

| Method | Endpoint | Role | Description |
|---|---|---|---|
| GET | `/api/halls` | PUBLIC | Get all cinema halls |
| GET | `/api/halls/{id}` | PUBLIC | Get hall by ID |
| GET | `/api/halls/{id}/seats` | PUBLIC | Get all seats in a hall |
| POST | `/api/halls` | ADMIN | Create cinema hall |
| PUT | `/api/halls/{id}` | ADMIN | Update cinema hall |
| DELETE | `/api/halls/{id}` | ADMIN | Delete cinema hall |

---

## 🪑 Seat — Mixed

| Method | Endpoint | Role | Description |
|---|---|---|---|
| GET | `/api/seats/{id}` | ADMIN | Get seat by ID |
| PUT | `/api/seats/{id}/type` | ADMIN | Update seat type |

---

## 🕐 Showtime — Mixed

| Method | Endpoint | Role | Description |
|---|---|---|---|
| GET | `/api/showtimes` | PUBLIC | Get all showtimes |
| GET | `/api/showtimes/{id}` | PUBLIC | Get showtime by ID |
| GET | `/api/showtimes/{id}/seats/available` | PUBLIC | Get available seats for showtime |
| POST | `/api/showtimes` | ADMIN | Create showtime |
| PUT | `/api/showtimes/{id}` | ADMIN | Update showtime |
| DELETE | `/api/showtimes/{id}` | ADMIN | Delete showtime |

---

## 🎟 Booking — Protected

| Method | Endpoint | Role | Description |
|---|---|---|---|
| POST | `/api/bookings` | CUSTOMER / ADMIN | Create a booking |
| GET | `/api/bookings/my` | CUSTOMER / ADMIN | Get own bookings |
| PUT | `/api/bookings/{id}/cancel` | CUSTOMER / ADMIN | Cancel a booking |

---

## 📦 Request / Response Examples

### Register
```json
POST /auth/register
{
    "firstName": "John",
    "lastName": "Doe",
    "email": "john@example.com",
    "password": "John1234!"
}
```

### Login
```json
POST /auth/login
{
    "email": "john@example.com",
    "password": "John1234!"
}

Response:
{
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "id": 1,
    "email": "john@example.com",
    "role": "ROLE_CUSTOMER"
}
```

### Create Booking
```json
POST /api/bookings
Authorization: Bearer <token>

{
    "showtimeId": 1,
    "seatIds": [1, 2, 3]
}

Response:
{
    "id": 1,
    "userId": 2,
    "userEmail": "john@example.com",
    "showtimeId": 1,
    "movieTitle": "The Fast and the Furious",
    "seatNumbers": ["A1", "A2", "A3"],
    "totalPrice": 16.50,
    "status": "CONFIRMED",
    "bookedAt": "2026-05-01T10:00:00"
}
```

### Error Responses

| Status | Meaning |
|---|---|
| 400 | Bad Request — validation error |
| 401 | Unauthorized — missing or invalid token |
| 403 | Forbidden — insufficient role |
| 404 | Not Found — resource does not exist |
| 409 | Conflict — seat already booked / resource already exists |
