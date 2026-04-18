# Dunder Mifflin Backend

Production-ready Spring Boot 3 API based on the provided OpenAPI (scripts/df.yaml) and DB schema (scripts/df.sql).

## Requirements
- Java 21+
- Gradle 8+

## Build
```
./gradlew clean build
```
On Windows PowerShell:
```
./gradlew.bat clean build
```

## Run
```
./gradlew bootRun
```

Application will start at http://localhost:8080

## Security
- HTTP Basic Auth with in-memory users:
  - user / user123 (ROLE_USER)
  - admin / admin123 (ROLE_ADMIN)
- RBAC:
  - GET /api/v1/catalogs and /api/v1/inventory accessible to USER and ADMIN
  - POST/PUT/PATCH/DELETE under /api/v1/** restricted to ADMIN

## CORS
Allowed origins (global): http://localhost:5173, http://localhost:3000, and their 127.0.0.1 equivalents.

## API Docs
- Swagger UI: http://localhost:8080/swagger-ui/index.html
- API Root: http://localhost:8080/api/v1

## Database
- H2 in-memory configured, initialized by src/main/resources/data.sql (adapted from scripts/df.sql).
- To switch to MySQL, update application.properties datasource and include MySQL driver (already present as runtimeOnly).

## Tests
Run all tests:
```
./gradlew test
```

Includes unit and MVC slice tests verifying service logic and security permissions.

## Notes
- DTOs and Bean Validation are used for inputs; errors are normalized via a global @ControllerAdvice and ApiError schema.
- Pagination via Spring Data Pageable on list endpoints. Default size=20, max size=100. One-indexed page parameters supported (?page=1). Added DB indexes for name/product/branch to scale reads.
