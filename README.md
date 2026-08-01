# SmartAttendenceBackend

Spring Boot backend for a smart attendance management system. The application exposes REST APIs for authentication, students, teachers, departments, sessions, subjects, and attendance tracking.

## Tech Stack

- Java 17
- Spring Boot
- Spring Web
- Spring Data JPA
- Spring Security
- JWT authentication
- MySQL
- Gradle

## Prerequisites

- Java 17 or later
- MySQL 8+ running locally
- Gradle Wrapper (`gradlew` / `gradlew.bat`)

## Configuration

The application reads its settings from `src/main/resources/application.properties`.

Before running, make sure you update the database connection values for your environment:

- `spring.datasource.url`
- `spring.datasource.username`
- `spring.datasource.password`

You should also replace the JWT secret with a secure value for your deployment.

## Run Locally

### Windows

```powershell
.\gradlew.bat bootRun
```

### macOS / Linux

```bash
./gradlew bootRun
```

The server starts on port `8080` by default.

## Run Tests

### Windows

```powershell
.\gradlew.bat test
```

### macOS / Linux

```bash
./gradlew test
```

## Main API Endpoints

### Authentication
- `POST /api/auth/login`

### Attendance
- `GET /api/attendance`
- `GET /api/attendance/{id}`
- `POST /api/attendance`
- `PUT /api/attendance/{id}`
- `DELETE /api/attendance/{id}`
- `GET /api/attendance/student/{studentId}`
- `GET /api/attendance/subject/{subjectId}`
- `GET /api/attendance/teacher/{teacherId}`
- `GET /api/attendance/status/{status}`
- `GET /api/attendance/date/{attendanceDate}`
- `GET /api/attendance/filter`
- `GET /api/attendance/reports/student/{studentId}`

### Department
- `GET /api/department`
- `GET /api/department/{id}`
- `POST /api/department`
- `PUT /api/department/{id}`
- `DELETE /api/department/{id}`

### Session
- `GET /api/session`
- `GET /api/session/{id}`
- `POST /api/session`
- `PUT /api/session/{id}`
- `DELETE /api/session/{id}`

### Student
- `GET /api/student`
- `GET /api/student/{id}`
- `POST /api/student`
- `PUT /api/student/{id}`
- `DELETE /api/student/{id}`

### Subject
- `GET /api/subject`
- `GET /api/subject/{id}`
- `POST /api/subject`
- `PUT /api/subject/{id}`
- `DELETE /api/subject/{id}`

### Teacher
- `GET /api/teacher`
- `GET /api/teacher/{id}`
- `POST /api/teacher`
- `PUT /api/teacher/{id}`
- `DELETE /api/teacher/{id}`

## Notes

- The project uses MySQL with JPA entity auto-update enabled in the default configuration.
- The `README.md` is intentionally kept lightweight so it can be expanded later with request/response examples or database schema details.

