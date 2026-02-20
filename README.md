# Family Wishlist REST API

Secure REST API built with Spring Boot and Spring Security.
The application allows users to manage a family, its members, and their wishes, with a strong focus on security, role-based access, and data isolation.

The project is designed as a backend-first application and prepared for future React integration.

---

## Tech Stack

- Docker
- Java 17
- Spring Boot
- Spring Security + JWT
- Hibernate / JPA
- PostgreSQL
- Jakarta Validation
- REST API

---

## Features

### User

- User registration and authentication using JWT
- Create and manage a family
- Add family members
- Create and manage wishes
- Access only own data (strict data isolation)
- Validation and meaningful error responses

### Admin

- View all registered users
- Delete users with cascade removal of related data
- No access to user domain data (families, persons, wishes)

> Admin role is system-level and follows the **least privilege principle**.

---

## Security Model

- Stateless authentication using JWT
- Role-based access control (USER, ADMIN)
- Method-level authorization with @PreAuthorize
- Admin permissions are limited to user management only
- Domain data is accessible exclusively by its owner

This approach reflects real-world backend security practices rather than tutorial-style implementations.

---

## API Overview

### Authentication

- POST /auth/register
- POST /auth/login

### User (ROLE_USER)

- GET /users/me
- PATCH /users/me/username
- PATCH /users/me/password
- GET /users/me/family
- DELETE /users/me
- POST /families
- GET /families
- DELETE /families
- POST /persons
- GET /persons/{personId}
- GET /persons/family/{familyId}
- DELETE /persons/{personId}
- POST /wishes
- GET /wishes/byOwner/{personId}
- PATCH /wishes/{wishId}/fulfill
- PUT /wishes/{wishId}
- DELETE /wishes/{wishId}

### Admin (ROLE_ADMIN)

- GET /admin/users
- DELETE /admin/users/{userId}

---

## Architectural Notes

- Clear separation between system-level (Admin) and domain-level (User) responsibilities
- Centralized exception handling with consistent error responses
- Transactional data management
- Controlled cascade deletion for related entities
- DTO-based API layer (entities are not exposed)

---

## How to Run

1. Configure PostgreSQL database
2. Update `application.properties` with database credentials, JWT secret and ADMIN password on `application-example.properties`
3. Run the application using your IDE or:

```bash
./mvnw spring-boot:run
```

## Running with Docker

You can run the application with PostgreSQL and pgAdmin using Docker Compose.

1. Configure the environment file (if using .env) with access rights to the PostgreSQL database, pgAdmin, and the application.
2. Build and start containers:

```bash
docker compose up --build
```

This will start:

app – Spring Boot on http://localhost:8080

db – PostgreSQL on localhost:5433

pgadmin – pgAdmin on http://localhost:5050 (use email/password from .env)

3. For stop and remove containers:

```bash
docker compose down -v
```

-v also removes volumes, useful for starting with a fresh database.

---

## Project Status

The backend is feature-complete and production-ready.
A React frontend will be added as a separate step.

---

## Why this project?

This project demonstrates:

- backend architecture design
- security-conscious development
- real-world role separation
- clean REST API practices

It is intended as a portfolio backend project rather than a tutorial exercise.

---

## Author

Oleksii Trufanov ([GitHub](https://github.com/Skif-DN))
