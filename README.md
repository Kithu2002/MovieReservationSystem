# Movie Reservation System API

A comprehensive Spring Boot REST API for managing movie theater reservations, built with Java 17 and MySQL.

## 🎬 Features

- **User Authentication & Authorization**: JWT-based authentication with role-based access control (USER and ADMIN roles)
- **Movie Management**: Create, update, delete, and browse movies with genre categorization
- **Showtime Management**: Schedule movie showtimes across different theaters
- **Seat Reservation System**: Real-time seat availability checking and reservation
- **Multiple Theater Support**: Manage multiple theaters with different seat configurations
- **Seat Type Pricing**: Different pricing tiers (REGULAR, PREMIUM, VIP)
- **Reservation Management**: Book, view, and cancel reservations
- **Admin Dashboard**: Revenue reports, occupancy analytics, and movie performance metrics
- **Data Seeding**: Automatic initialization with sample data for testing

## 🛠️ Technology Stack

- **Backend Framework**: Spring Boot 3.5.0
- **Language**: Java 17
- **Database**: MySQL 8.0
- **Security**: Spring Security with JWT
- **ORM**: Spring Data JPA with Hibernate
- **Build Tool**: Maven
- **Additional Libraries**:
  - Lombok (for reducing boilerplate code)
  - JJWT (for JWT token handling)
  - Jakarta Validation (for request validation)

## 📋 Prerequisites

- JDK 17 or higher
- MySQL 8.0 or higher
- Maven 3.6 or higher
- An IDE (IntelliJ IDEA, Eclipse, or VS Code)

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone <repository-url>
cd Movie-Reservation
```

### 2. Database Setup

Create a MySQL database:

```sql
CREATE DATABASE MovieReservation_System;
```

### 3. Configure Application Properties

Update `src/main/resources/application.properties` with your database credentials:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/MovieReservation_System
spring.datasource.username=root
spring.datasource.password=yourPassword
```

### 4. Build and Run

Using Maven wrapper:

```bash
./mvnw clean install
./mvnw spring-boot:run
```

Or using Maven:

```bash
mvn clean install
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

## 🔐 Default Admin Credentials

Upon first run, the application creates a default admin user:

- **Email**: admin@moviereservation.com
- **Password**: admin123

---

# 📚 API Documentation

## Base URL
```
http://localhost:8080/api
```

## Authentication
Most endpoints require authentication. Include the JWT token in the Authorization header:
```
Authorization: Bearer <your-jwt-token>
```

---

## 🔐 Authentication Endpoints

### 1. User Registration
Creates a new user account with USER role.

**Endpoint:** `POST /auth/signup`  
**Access:** Public  
**Request Body:**
```json
{
  "email": "john.doe@example.com",
  "password": "SecurePass123!",
  "firstName": "John",
  "lastName": "Doe"
}
```

**Validation Rules:**
- Email: Must be valid email format
- Password: Minimum 6 characters, maximum 40 characters
- First Name: Required, not blank
- Last Name: Required, not blank

**Success Response:** `200 OK`
```json
"User registered successfully"
```

**Error Responses:**
- `400 Bad Request`: Validation errors or email already exists
```json
{
  "error": "Email is already taken!"
}
```

### 2. User Login
Authenticates user and returns JWT token.

**Endpoint:** `POST /auth/login`  
**Access:** Public  
**Request Body:**
```json
{
  "email": "john.doe@example.com",
  "password": "SecurePass123!"
}
```

**Success Response:** `200 OK`
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJqb2huLmRvZUBleGFtcGxlLmNvbSIsImlhdCI6MTY4...",
  "type": "Bearer",
  "email": "john.doe@example.com",
  "roles": ["ROLE_USER"]
}
```

**Error Responses:**
- `401 Unauthorized`: Invalid credentials
- `400 Bad Request`: Missing required fields

---

## 🎬 Movie Management Endpoints

### 1. Get All Active Movies
Retrieves all movies marked as active.

**Endpoint:** `GET /movies/public`  
**Access:** Public  
**Response:** `200 OK`
```json
[
  {
    "id": 1,
    "title": "Inception",
    "description": "A thief who steals corporate secrets through dream-sharing technology...",
    "posterImageUrl": "https://example.com/inception-poster.jpg",
    "duration": 148,
    "genres": ["Sci-Fi", "Action", "Thriller"],
    "isActive": true
  },
  {
    "id": 2,
    "title": "The Dark Knight",
    "description": "When the menace known as the Joker wreaks havoc...",
    "posterImageUrl": "https://example.com/dark-knight-poster.jpg",
    "duration": 152,
    "genres": ["Action", "Crime", "Drama"],
    "isActive": true
  }
]
```

### 2. Get Movie by ID
Retrieves detailed information about a specific movie.

**Endpoint:** `GET /movies/public/{id}`  
**Access:** Public  
**Path Parameters:**
- `id` (Long): Movie ID

**Success Response:** `200 OK`
```json
{
  "id": 1,
  "title": "Inception",
  "description": "A thief who steals corporate secrets through dream-sharing technology...",
  "posterImageUrl": "https://example.com/inception-poster.jpg",
  "duration": 148,
  "genres": ["Sci-Fi", "Action", "Thriller"],
  "isActive": true
}
```

**Error Response:**
- `404 Not Found`: Movie not found

### 3. Get Movies by Genre
Retrieves all active movies of a specific genre.

**Endpoint:** `GET /movies/public/genre/{genre}`  
**Access:** Public  
**Path Parameters:**
- `genre` (String): Genre name (e.g., "Action", "Sci-Fi", "Drama")

**Success Response:** `200 OK`
```json
[
  {
    "id": 1,
    "title": "Inception",
    "description": "A thief who steals corporate secrets...",
    "posterImageUrl": "https://example.com/inception-poster.jpg",
    "duration": 148,
    "genres": ["Sci-Fi", "Action", "Thriller"],
    "isActive": true
  }
]
```

### 4. Create Movie (Admin Only)
Creates a new movie in the system.

**Endpoint:** `POST /movies`  
**Access:** Admin only  
**Request Body:**
```json
{
  "title": "Interstellar",
  "description": "A team of explorers travel through a wormhole in space...",
  "posterImageUrl": "https://example.com/interstellar-poster.jpg",
  "duration": 169,
  "genres": ["Sci-Fi", "Adventure", "Drama"],
  "isActive": true
}
```

**Success Response:** `200 OK`
```json
{
  "id": 3,
  "title": "Interstellar",
  "description": "A team of explorers travel through a wormhole in space...",
  "posterImageUrl": "https://example.com/interstellar-poster.jpg",
  "duration": 169,
  "genres": ["Sci-Fi", "Adventure", "Drama"],
  "isActive": true
}
```

**Error Responses:**
- `403 Forbidden`: User doesn't have admin role
- `400 Bad Request`: Invalid data

### 5. Update Movie (Admin Only)
Updates an existing movie's information.

**Endpoint:** `PUT /movies/{id}`  
**Access:** Admin only  
**Path Parameters:**
- `id` (Long): Movie ID

**Request Body:**
```json
{
  "title": "Interstellar (IMAX Edition)",
  "description": "Updated description...",
  "posterImageUrl": "https://example.com/interstellar-imax-poster.jpg",
  "duration": 169,
  "genres": ["Sci-Fi", "Adventure", "Drama", "IMAX"],
  "isActive": true
}
```

**Success Response:** `200 OK` (Returns updated movie)

**Error Responses:**
- `404 Not Found`: Movie not found
- `403 Forbidden`: User doesn't have admin role

### 6. Delete Movie (Admin Only)
Soft deletes a movie (sets isActive to false).

**Endpoint:** `DELETE /movies/{id}`  
**Access:** Admin only  
**Path Parameters:**
- `id` (Long): Movie ID

**Success Response:** `200 OK`
```json
"Movie deleted successfully"
```

**Error Responses:**
- `404 Not Found`: Movie not found
- `403 Forbidden`: User doesn't have admin role

---

## 🎭 Showtime Management Endpoints

### 1. Get Movie Showtimes
Retrieves all upcoming showtimes for a specific movie.

**Endpoint:** `GET /movies/public/{movieId}/showtimes`  
**Access:** Public  
**Path Parameters:**
- `movieId` (Long): Movie ID

**Success Response:** `200 OK`
```json
[
  {
    "id": 1,
    "movieId": 1,
    "movieTitle": "Inception",
    "theaterId": 1,
    "theaterName": "Main Theater",
    "startTime": "2024-06-15T19:30:00",
    "endTime": "2024-06-15T22:00:00",
    "price": 12.50,
    "availableSeats": 85
  },
  {
    "id": 2,
    "movieId": 1,
    "movieTitle": "Inception",
    "theaterId": 2,
    "theaterName": "VIP Theater",
    "startTime": "2024-06-15T20:00:00",
    "endTime": "2024-06-15T22:30:00",
    "price": 15.00,
    "availableSeats": 45
  }
]
```

### 2. Get Showtimes by Date
Retrieves all showtimes for a specific date.

**Endpoint:** `GET /movies/public/showtimes/date`  
**Access:** Public  
**Query Parameters:**
- `date` (LocalDate): Date in ISO format (YYYY-MM-DD)

**Example Request:**
```
GET /movies/public/showtimes/date?date=2024-06-15
```

**Success Response:** `200 OK`
```json
[
  {
    "id": 1,
    "movieId": 1,
    "movieTitle": "Inception",
    "theaterId": 1,
    "theaterName": "Main Theater",
    "startTime": "2024-06-15T14:00:00",
    "endTime": "2024-06-15T16:30:00",
    "price": 10.00,
    "availableSeats": 90
  },
  {
    "id": 3,
    "movieId": 2,
    "movieTitle": "The Dark Knight",
    "theaterId": 1,
    "theaterName": "Main Theater",
    "startTime": "2024-06-15T17:00:00",
    "endTime": "2024-06-15T19:35:00",
    "price": 12.50,
    "availableSeats": 75
  }
]
```

### 3. Create Showtime (Admin Only)
Schedules a new showtime for a movie.

**Endpoint:** `POST /showtimes`  
**Access:** Admin only  
**Request Body:**
```json
{
  "movieId": 1,
  "theaterId": 1,
  "startTime": "2024-06-20T19:30:00",
  "price": 12.50
}
```

**Note:** End time is automatically calculated based on movie duration.

**Success Response:** `200 OK`
```json
{
  "id": 4,
  "movieId": 1,
  "movieTitle": "Inception",
  "theaterId": 1,
  "theaterName": "Main Theater",
  "startTime": "2024-06-20T19:30:00",
  "endTime": "2024-06-20T22:00:00",
  "price": 12.50,
  "availableSeats": 100
}
```

**Error Responses:**
- `404 Not Found`: Movie or theater not found
- `403 Forbidden`: User doesn't have admin role

### 4. Update Showtime (Admin Only)
Updates an existing showtime.

**Endpoint:** `PUT /showtimes/{id}`  
**Access:** Admin only  
**Path Parameters:**
- `id` (Long): Showtime ID

**Request Body:** (All fields optional)
```json
{
  "startTime": "2024-06-20T20:00:00",
  "price": 15.00
}
```

**Success Response:** `200 OK` (Returns updated showtime)

### 5. Delete Showtime (Admin Only)
Removes a showtime from the system.

**Endpoint:** `DELETE /showtimes/{id}`  
**Access:** Admin only  
**Path Parameters:**
- `id` (Long): Showtime ID

**Success Response:** `200 OK`
```json
"Showtime deleted successfully"
```

---

## 🎟️ Reservation Endpoints

### 1. Get Available Seats
Retrieves all seats for a showtime with availability status.

**Endpoint:** `GET /reservations/seats/{showtimeId}`  
**Access:** Authenticated users  
**Path Parameters:**
- `showtimeId` (Long): Showtime ID

**Success Response:** `200 OK`
```json
[
  {
    "id": 1,
    "seatNumber": "A1",
    "type": "VIP",
    "isAvailable": true,
    "price": 25.00
  },
  {
    "id": 2,
    "seatNumber": "A2",
    "type": "VIP",
    "isAvailable": false,
    "price": 25.00
  },
  {
    "id": 21,
    "seatNumber": "C1",
    "type": "PREMIUM",
    "isAvailable": true,
    "price": 18.75
  },
  {
    "id": 51,
    "seatNumber": "F1",
    "type": "REGULAR",
    "isAvailable": true,
    "price": 12.50
  }
]
```

**Seat Pricing:**
- VIP: Base price × 2
- PREMIUM: Base price × 1.5
- REGULAR: Base price

### 2. Create Reservation
Books selected seats for a showtime.

**Endpoint:** `POST /reservations`  
**Access:** Authenticated users  
**Request Body:**
```json
{
  "showtimeId": 1,
  "seatIds": [1, 2, 3]
}
```

**Success Response:** `200 OK`
```json
{
  "id": 1,
  "userId": 2,
  "userName": "John Doe",
  "showtimeId": 1,
  "movieTitle": "Inception",
  "theaterName": "Main Theater",
  "showtime": "2024-06-15T19:30:00",
  "seatNumbers": ["A1", "A2", "A3"],
  "reservationTime": "2024-06-10T15:30:45",
  "totalAmount": 75.00,
  "status": "CONFIRMED"
}
```

**Error Responses:**
- `400 Bad Request`: Seats already reserved or invalid seat IDs
- `400 Bad Request`: Cannot book for past showtimes
- `404 Not Found`: Showtime not found

### 3. Get My Reservations
Retrieves all reservations for the authenticated user.

**Endpoint:** `GET /reservations/my`  
**Access:** Authenticated users  

**Success Response:** `200 OK`
```json
[
  {
    "id": 1,
    "userId": 2,
    "userName": "John Doe",
    "showtimeId": 1,
    "movieTitle": "Inception",
    "theaterName": "Main Theater",
    "showtime": "2024-06-15T19:30:00",
    "seatNumbers": ["A1", "A2", "A3"],
    "reservationTime": "2024-06-10T15:30:45",
    "totalAmount": 75.00,
    "status": "CONFIRMED"
  },
  {
    "id": 2,
    "userId": 2,
    "userName": "John Doe",
    "showtimeId": 3,
    "movieTitle": "The Dark Knight",
    "theaterName": "VIP Theater",
    "showtime": "2024-06-12T20:00:00",
    "seatNumbers": ["B5", "B6"],
    "reservationTime": "2024-06-08T10:15:30",
    "totalAmount": 30.00,
    "status": "CANCELLED"
  }
]
```

### 4. Get My Upcoming Reservations
Retrieves only future reservations for the authenticated user.

**Endpoint:** `GET /reservations/my/upcoming`  
**Access:** Authenticated users  

**Success Response:** `200 OK`
```json
[
  {
    "id": 1,
    "userId": 2,
    "userName": "John Doe",
    "showtimeId": 1,
    "movieTitle": "Inception",
    "theaterName": "Main Theater",
    "showtime": "2024-06-15T19:30:00",
    "seatNumbers": ["A1", "A2", "A3"],
    "reservationTime": "2024-06-10T15:30:45",
    "totalAmount": 75.00,
    "status": "CONFIRMED"
  }
]
```

### 5. Cancel Reservation
Cancels a reservation and frees up the seats.

**Endpoint:** `DELETE /reservations/{id}`  
**Access:** Authenticated users (own reservations only)  
**Path Parameters:**
- `id` (Long): Reservation ID

**Success Response:** `200 OK`
```json
"Reservation cancelled successfully"
```

**Error Responses:**
- `404 Not Found`: Reservation not found
- `403 Forbidden`: Cannot cancel other users' reservations
- `400 Bad Request`: Cannot cancel past reservations

---

## 👨‍💼 Admin Endpoints

### 1. Promote User to Admin
Grants admin role to an existing user.

**Endpoint:** `POST /admin/users/{userId}/promote`  
**Access:** Admin only  
**Path Parameters:**
- `userId` (Long): User ID to promote

**Success Response:** `200 OK`
```json
"User promoted to admin successfully"
```

**Error Responses:**
- `404 Not Found`: User not found
- `403 Forbidden`: Insufficient permissions

### 2. Get Revenue Report
Generates a comprehensive revenue report.

**Endpoint:** `GET /admin/reports/revenue`  
**Access:** Admin only  

**Success Response:** `200 OK`
```json
{
  "totalRevenue": 12500.75,
  "revenueByMovie": {
    "Inception": 5500.00,
    "The Dark Knight": 4200.50,
    "Interstellar": 2800.25
  },
  "totalReservations": 145
}
```

### 3. Get Movie Performance Report
Generates detailed analytics for a specific movie.

**Endpoint:** `GET /admin/reports/movie/{movieId}`  
**Access:** Admin only  
**Path Parameters:**
- `movieId` (Long): Movie ID

**Success Response:** `200 OK`
```json
{
  "movieTitle": "Inception",
  "totalShowtimes": 12,
  "totalCapacity": 1200,
  "totalReservedSeats": 850,
  "occupancyRate": 70.83,
  "totalRevenue": 5500.00
}
```

### 4. Get Theater Occupancy Report
Generates occupancy statistics for all theaters.

**Endpoint:** `GET /admin/reports/occupancy`  
**Access:** Admin only  

**Success Response:** `200 OK`
```json
{
  "theaterOccupancy": {
    "Main Theater": {
      "totalSeats": 100,
      "totalShowtimes": 25,
      "totalCapacity": 2500,
      "totalReserved": 1875,
      "occupancyRate": 75.0
    },
    "VIP Theater": {
      "totalSeats": 50,
      "totalShowtimes": 20,
      "totalCapacity": 1000,
      "totalReserved": 680,
      "occupancyRate": 68.0
    }
  }
}
```

---

## 🏗️ Database Schema

### Main Entities

- **User**: Stores user information and authentication details
- **Role**: Defines user roles (USER, ADMIN)
- **Movie**: Movie information with genres
- **Genre**: Movie categories
- **Theater**: Theater information with seat capacity
- **Seat**: Individual seats with types (REGULAR, PREMIUM, VIP)
- **Showtime**: Movie schedule in specific theaters
- **Reservation**: User bookings with status
- **ReservedSeat**: Links reservations to specific seats

## 🔒 Security Considerations

- JWT tokens expire after 24 hours (configurable)
- Passwords are encrypted using BCrypt
- Role-based access control for admin operations
- Secure API endpoints with proper authentication

## 🚦 HTTP Status Codes

- `200 OK`: Successful request
- `201 Created`: Resource created successfully
- `400 Bad Request`: Invalid request data
- `401 Unauthorized`: Authentication required
- `403 Forbidden`: Insufficient permissions
- `404 Not Found`: Resource not found
- `500 Internal Server Error`: Server error

## 📝 Important Notes

- **Soft Delete**: Movies are not physically deleted; `isActive` is set to false
- **Showtime Constraints**: Cannot book seats for past showtimes
- **Seat Pricing**: Automatically calculated based on seat type and base showtime price
- **Reservation Cancellation**: Only allowed for future showtimes
- **User Permissions**: Users can only manage their own reservations

## 👥 Authors

-Kithuryan Vinayagaindran

## 🙏 Acknowledgments

- Spring Boot documentation
- JWT.io for token handling
- MySQL for database management
