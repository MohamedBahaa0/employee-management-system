# Login CRUD - Employee Management System

A Spring Boot REST API application for managing employees and their tasks with authentication and authorization support.

## Overview

Login CRUD is a lightweight employee management system built with Spring Boot that provides RESTful APIs for performing CRUD operations on employee records. It includes features like user authentication, data validation, CSV import functionality, and a PostgreSQL database backend.

## Tech Stack

- **Framework**: Spring Boot 4.0.5
- **Java Version**: 17
- **Database**: PostgreSQL
- **Data Mapping**: MapStruct 1.5.5
- **Security**: Spring Security 6
- **Template Engine**: Thymeleaf
- **ORM**: Spring Data JPA & Hibernate
- **Build Tool**: Maven
- **Container**: Docker & Docker Compose

## Features

- **Employee Management**: Create, read, update, and delete employee records
- **Task Management**: Associate tasks with employees (one-to-many relationship)
- **REST API**: Well-documented REST endpoints for all operations
- **Authentication**: Basic HTTP authentication for API access
- **Validation**: Request body validation using Jakarta validation
- **CSV Import**: Bulk import employee data from CSV files
- **Pagination**: Paginated employee listing with configurable page sizes
- **DTO Pattern**: Clean separation using Data Transfer Objects
- **Exception Handling**: Global exception handling (EmployeeNotFoundException)
- **Development Tools**: Spring DevTools for faster development

## Project Structure

```
src/main/java/com/logincrud/
├── LogincrudApplication.java          # Main Spring Boot application
├── config/
│   └── SecurityConfig.java            # Spring Security configuration
├── controller/
│   └── EmployeeController.java        # REST API endpoints
├── service/
│   ├── EmployeeService.java           # Service interface
│   └── EmployeeServiceImpl.java        # Service implementation
├── repository/
│   └── EmployeeRepository.java        # Data access layer
├── model/
│   ├── Employee.java                  # Employee entity
│   └── Task.java                      # Task entity
├── dto/
│   ├── EmployeeRequestDTO.java        # Request DTO
│   └── EmployeeResponseDTO.java       # Response DTO
├── mapper/
│   └── EmployeeMapper.java            # MapStruct mapper
└── exception/
    ├── EmployeeNotFoundException.java  # Custom exception
    └── GlobalExceptionHandler.java    # Global error handler

src/main/resources/
├── application.properties              # Application configuration
└── templates/                          # Thymeleaf templates
    ├── home.html
    ├── users.html
    └── form.html
```

## Prerequisites

- Java 17 or higher
- Maven 3.6+
- PostgreSQL 12+
- Docker & Docker Compose (optional, for containerized PostgreSQL)

## Installation & Setup

### 1. Clone or Download the Project

```bash
cd Desktop/Java/GettingStarted/logincrud
```

### 2. Configure Database Connection

Edit `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/mydb
spring.datasource.username=myuser
spring.datasource.password=mypass
```

### 3. Start PostgreSQL Database

#### Option A: Using Docker Compose (Recommended)

```bash
docker-compose up -d
```

This will start a PostgreSQL container with the following credentials:
- Database: `mydb`
- Username: `myuser`
- Password: `mypass`
- Port: `5432`

#### Option B: Manual PostgreSQL Installation

Ensure PostgreSQL is running and create the database:

```sql
CREATE DATABASE mydb;
CREATE USER myuser WITH PASSWORD 'mypass';
GRANT ALL PRIVILEGES ON DATABASE mydb TO myuser;
```

## Running the Application

### Using Maven

```bash
# Clean and build
mvn clean install

# Run the application
mvn spring-boot:run
```

The application will start on `http://localhost:8080`

### Using IDE

- Open the project in your IDE (IntelliJ IDEA, Eclipse, etc.)
- Run `LogincrudApplication.java` as a Java application

## API Endpoints

All endpoints require Basic HTTP Authentication.

### Employee Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/employees` | Get all employees (paginated) |
| GET | `/api/employees/{id}` | Get employee by ID |
| POST | `/api/employees` | Create a new employee |
| PUT | `/api/employees/{id}` | Update an employee |
| DELETE | `/api/employees/{id}` | Delete an employee |
| POST | `/api/employees/import` | Import employees from CSV |

### Query Parameters

- **Pagination** (GET `/api/employees`):
  - `page` (default: 0) - Page number
  - `size` (default: 5) - Page size

### Example Requests

#### Get All Employees
```bash
curl -X GET "http://localhost:8080/api/employees?page=0&size=10" \
  -H "Authorization: Basic dXNlcm5hbWU6cGFzc3dvcmQ="
```

#### Get Employee by ID
```bash
curl -X GET "http://localhost:8080/api/employees/1" \
  -H "Authorization: Basic dXNlcm5hbWU6cGFzc3dvcmQ="
```

#### Create Employee
```bash
curl -X POST "http://localhost:8080/api/employees" \
  -H "Content-Type: application/json" \
  -H "Authorization: Basic dXNlcm5hbWU6cGFzc3dvcmQ=" \
  -d '{
    "name": "John Doe",
    "email": "john@example.com",
    "age": 30
  }'
```

#### Update Employee
```bash
curl -X PUT "http://localhost:8080/api/employees/1" \
  -H "Content-Type: application/json" \
  -H "Authorization: Basic dXNlcm5hbWU6cGFzc3dvcmQ=" \
  -d '{
    "name": "Jane Doe",
    "email": "jane@example.com",
    "age": 28
  }'
```

#### Delete Employee
```bash
curl -X DELETE "http://localhost:8080/api/employees/1" \
  -H "Authorization: Basic dXNlcm5hbWU6cGFzc3dvcmQ="
```

#### Import from CSV
```bash
curl -X POST "http://localhost:8080/api/employees/import" \
  -H "Authorization: Basic dXNlcm5hbWU6cGFzc3dvcmQ=" \
  -F "file=@employees.csv"
```

## Data Models

### Employee
```java
{
  "id": 1,
  "name": "John Doe",
  "email": "john@example.com",
  "age": 30,
  "tasks": [...]
}
```

### Task
```java
{
  "id": 1,
  "title": "Complete project",
  "employee": {...}
}
```

## Database Schema

### Employee Table
```sql
CREATE TABLE employee (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255) NOT NULL,
  email VARCHAR(255),
  age INT
);
```

### Task Table
```sql
CREATE TABLE task (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(255),
  employee_id BIGINT,
  FOREIGN KEY (employee_id) REFERENCES employee(id)
);
```

## Configuration

### Spring Security

- **Default**: Basic HTTP Authentication
- **CSRF**: Disabled (API-focused)
- **Session Management**: Stateless
- **Public Endpoints**: `/auth/**` routes
- **Protected Endpoints**: All `/api/**` endpoints require authentication

### Hibernate/JPA

- **DDL Mode**: `update` (auto-creates/updates tables)
- **SQL Logging**: Enabled (`show-sql=true`)
- **Dialect**: PostgreSQL

## Docker Compose

The `docker-compose.yml` file provides a PostgreSQL container with persistent volume:

```yaml
services:
  db:
    image: postgres:latest
    container_name: my-postgres
    environment:
      POSTGRES_DB: mydb
      POSTGRES_USER: myuser
      POSTGRES_PASSWORD: mypass
    ports:
      - "5432:5432"
    volumes:
      - pgdata:/var/lib/postgresql
```

## Development Tools

- **Spring DevTools**: Enabled for hot reload during development
- **Lombok**: Reduces boilerplate code with annotations
- **MapStruct**: Code generation for object mapping
- **Spring Test**: Unit and integration testing support

## Building & Deployment

### Build JAR
```bash
mvn clean package
```

This creates `target/logincrud-0.0.1-SNAPSHOT.jar`

### Run JAR
```bash
java -jar target/logincrud-0.0.1-SNAPSHOT.jar
```

## Troubleshooting

### Database Connection Issues
- Ensure PostgreSQL is running
- Verify credentials in `application.properties`
- Check if port 5432 is accessible

### Port Already in Use
Change the port in `application.properties`:
```properties
server.port=8081
```

### Compilation Errors
Ensure annotation processors are enabled:
- MapStruct processor
- Lombok annotation processor

## License

Not specified

## Support

For issues or questions, refer to the Spring Boot documentation or create an issue in the project repository.
