# Caterpillar Mining Backend

## Summary

Caterpillar Mining Backend is a learning exercise implementing a RESTful API for the `mining`
bounded context of a Caterpillar-Mining-inspired platform, built with Java, Spring Boot and
Spring Data JPA on MySQL. It follows a Domain-Driven Design (DDD) layered architecture (domain,
application, infrastructure, interfaces) with tactical patterns (aggregates, value objects,
commands, CQRS-style command services) and includes OpenAPI documentation via Swagger UI.

This project is scoped as a focused CRUD slice inside a single bounded context, adapted from a
larger Caterpillar Mining case study, rather than a full multi-bounded-context submission.

## Features

- RESTful API
- OpenAPI documentation with Swagger UI
- Spring Boot Framework
- Spring Data JPA
- Domain-Driven Design (bounded contexts, aggregates, value objects, commands)
- Transparent field-level encryption (AES-256-GCM) for sensitive data at rest
- Centralized exception handling with consistent error responses

## Mining Bounded Context

The `mining` bounded context manages `MiningEquipmentUnit` aggregates: mining equipment tracked
by the platform, including their model, serial number, operational status, assigned mine site,
GPS location and accumulated hours of operation.

### Endpoints

All endpoints are rooted at `/api/v1/equipment-units`. The `id` (surrogate primary key) and
`equipmentUnitId` (a generated UUID) are never supplied by the client - both are generated
automatically at registration time and never change afterwards.

| Method | Path                          | Description                          |
|--------|-------------------------------|---------------------------------------|
| POST   | `/api/v1/equipment-units`     | Register a new equipment unit         |
| GET    | `/api/v1/equipment-units`     | List all equipment units              |
| GET    | `/api/v1/equipment-units/{id}`| Get one equipment unit by ID          |
| PUT    | `/api/v1/equipment-units/{id}`| Update an existing equipment unit     |
| DELETE | `/api/v1/equipment-units/{id}`| Delete an existing equipment unit     |

**`POST` / `PUT` request body**
```json
{
  "model": "Cat 793F",
  "serialNumber": "SN-793F-0001",
  "operationStatus": "ACTIVE",
  "assignedMineSite": "Antamina",
  "gpsLatitude": -9.5417,
  "gpsLongitude": -77.0619,
  "hoursOfOperation": 1200
}
```

**Response `201 Created` (POST) / `200 OK` (GET, PUT)**
```json
{
  "id": 1,
  "equipmentUnitId": "3f2504e0-4f89-41d3-9a0c-0305e82c3301",
  "model": "Cat 793F",
  "serialNumber": "SN-793F-0001",
  "operationStatus": "ACTIVE",
  "assignedMineSite": "Antamina",
  "gpsLatitude": -9.5417,
  "gpsLongitude": -77.0619,
  "hoursOfOperation": 1200,
  "createdAt": "2026-08-09T12:00:00.000+00:00",
  "updatedAt": "2026-08-09T12:00:00.000+00:00"
}
```

**`DELETE` response `200 OK`**
```json
{ "message": "Mining equipment unit with ID 1 was deleted successfully." }
```

**Error responses** (`400 Bad Request`, `404 Not Found`, or `409 Conflict`)
```json
{
  "message": "Invalid operation status 'RUNNING'. Allowed values: ACTIVE, IN_MAINTENANCE, INACTIVE.",
  "status": 400,
  "timestamp": "2026-08-09T12:00:00Z"
}
```

### Business rules implemented

- Two equipment units cannot share the same `serialNumber` within the same `assignedMineSite`
  (enforced per mine site, not globally). This is enforced both on creation and on update - an
  update excludes the unit being updated from the duplicate check against its own previous value.
- `gpsLocation` must be a valid geographic coordinate: latitude within `[-90, 90]`, longitude
  within `[-180, 180]`.
- `operationStatus` must be one of `ACTIVE`, `IN_MAINTENANCE`, `INACTIVE`.
- `equipmentUnitId` is a system-generated UUID, never accepted from the client, and cannot be
  changed by an update.
- `serialNumber` is encrypted at rest (see below) and transparently decrypted in API responses.
- Operations targeting a non-existent `id` (GET by ID, PUT, DELETE) return `404 Not Found`.

### Serial number encryption

`serialNumber` is encrypted at rest using AES-256-GCM via a JPA `AttributeConverter`
(`SerialNumberEncryptionConverter`, in the `shared` bounded context), and decrypted automatically
whenever the entity is loaded, so API responses always show the plain-text value.

Because each encryption uses a random initialization vector, the same plain-text value never
produces the same stored ciphertext twice. This is a deliberate security choice - it avoids
leaking equality information about stored values - but it also means the duplicate-serial-number
business rule cannot be enforced with a SQL equality check or a database unique constraint on the
encrypted column. Instead, it is enforced in the application layer (`EquipmentUnitCommandServiceImpl`)
by loading all equipment units already assigned to the target mine site (JPA transparently
decrypts each one) and comparing serial numbers in memory. This is a known trade-off: without a
database-level uniqueness guarantee or pessimistic locking, a race condition exists between two
concurrent requests for the same (mine site, serial number) pair; this is accepted as an
out-of-scope limitation for this learning exercise.

To run the project, a Base64-encoded AES-256 secret key must be configured for
`mining.encryption.serial-number.secret-key` (see `application-dev.properties`). Generate one with:

```bash
openssl rand -base64 32
```

## Running the project

`application-dev.properties` deliberately does not contain any real credentials - it reads them
from environment variables, so nothing sensitive ever needs to be committed to source control.

1. Ensure a MySQL server is running locally.
2. Set the following environment variables before starting the app:
   - `DB_PASSWORD` - your local MySQL root password (required, no default).
   - `MINING_ENCRYPTION_KEY` - a Base64-encoded AES-256 key (required, no default), e.g.
     generate one with `openssl rand -base64 32`.
   - `DB_USERNAME` (optional, defaults to `root`) and `DB_NAME` (optional, defaults to
     `caterpillar-db`) if your local setup differs from the defaults.

   In IntelliJ IDEA: open the Run/Debug Configuration for
   `CaterpillarMiningBackendApplication` and add them under **Environment variables**.
   From a terminal (PowerShell): `$env:DB_PASSWORD = "..."` etc., in the same session before
   running the app.
3. Run `mvnw spring-boot:run` (or run the application's main class from your IDE).
4. Browse the API documentation at `http://localhost:8080/swagger-ui/index.html`.

## Reference Documentation

* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/maven-plugin)
* [Spring Data JPA](https://docs.spring.io/spring-boot/reference/data/sql.html#data.sql.jpa-and-spring-data)
* [Validation](https://docs.spring.io/spring-boot/reference/io/validation.html)
* [springdoc-openapi](https://springdoc.org/)

## Author

Diego Vilca
