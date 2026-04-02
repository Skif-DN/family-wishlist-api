# Family Wishlist
Full-stack application to manage a family, its members, and their wishes.
Backend is built with Spring Boot + Spring Security + PostgreSQL, frontend is built with React, and the whole app can run via Docker.
---

## Tech Stack

- Backend: Java 17, Spring Boot, Spring Security + JWT, Hibernate/JPA, Jakarta Validation, REST API
- Frontend: React, React Router, React Icons, CSS
- Database: PostgreSQL
- Testing: JUnit 5, Mockito
- DevOps: Docker, Docker Compose

---

## Features
- Full-stack build: frontend is automatically built and bundled via Maven
### Users

- Register and login with JWT authentication
- Create and manage families and family members
- Create, edit, fulfill, and delete wishes
- View statistics (total and fulfilled wishes)
- Access restricted to own data (strict data isolation)

### Admin

- View all registered users
- Delete users with cascade removal of related data
- No access to user domain data (families, persons, wishes)

---

## Security

- Stateless authentication using JWT
- Role-based access (USER, ADMIN)
- Method-level authorization with @PreAuthorize
- Strict data isolation (users access only their own data)
---

## API Endpoints

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

## Frontend
- Responsive UI built with React
- Display and manage wishes, family members, and statistics
- Tooltips, modals, and buttons for interactions
- Sorting, filtering, mobile menu, scroll-to-top

 ---

## Architecture

- Clear separation between backend (domain and system-level roles) and frontend
- DTO-based API layer (entities not exposed directly)
- Transactional data management with controlled cascade deletion
- Centralized exception handling with meaningful error responses

---

## Running the Project

1. Configure PostgreSQL database or another 
2. Update application.properties with database credentials, JWT secret, and ADMIN password
3. Run application:

```bash
docker compose up --build
```
This will automatically:

- build the backend (Spring Boot)
- build the frontend (via Maven + React)
- bundle frontend into the backend
- start all services


4. For stop Docker and remove containers:

```bash
docker compose down
```
- For stop Docker and remove containers with volumes:

```bash
docker compose down -v
```
---
## Testing

All services and controllers are covered with unit and integration tests using JUnit 5 and Mockito.

Run tests with:

```bash
./mvnw test
```

---

## Project Status

- Full-stack application is production-ready
- Backend is feature-complete
- Frontend is fully integrated and served by Spring Boot
- Actively maintained with ongoing improvements in performance, UX, and code quality

---

## Author

Oleksii Trufanov ([GitHub](https://github.com/Skif-DN))
