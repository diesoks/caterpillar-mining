# Caterpillar Mining - Equipment Fleet Management

## About Caterpillar Mining

Since 1925, [Caterpillar](https://www.caterpillar.com/en/company.html) has been a global leader in
heavy machinery and advanced technology for the mining industry. Its Caterpillar Mining division
is recognized worldwide for its automation, remote monitoring and operations-optimization
solutions for both open-pit and underground mining.

Caterpillar offers operators, supervisors and mining management a wide range of digital solutions
for monitoring, analysis and operations management. Among these, the
[Cat® MineStar™](https://www.cat.com/en_US/by-industry/mining/minestar-solutions.html) ecosystem
stands out, integrating fleet tracking, predictive analytics, safety management, production
planning and support for critical operational decisions. Combined with onboard sensors, advanced
telemetry and cloud analytics platforms, these tools help mining operations improve efficiency,
reduce operating costs, anticipate failures, strengthen operational safety and keep equipment
availability at an optimal level.

## What this project is

This project implements the **equipment asset registry** that would sit at the foundation of a
system like Cat® MineStar™: a full CRUD (Create, Read, Update, Delete) for `MiningEquipmentUnit`
records - the digital record of each physical machine in a mining fleet, tracking its model,
serial number, real-time operational status, assigned mine site, GPS location and accumulated
hours of operation (the metric that drives Caterpillar's hour-based preventive maintenance
scheduling).

It is adapted from an academic case study ("Caso Caterpillar Mining", Desarrollo de Aplicaciones
Open Source) that originally required only a single `POST` endpoint on one bounded context. This
implementation extends that scope into a complete CRUD, with a full frontend and a fully
containerized deployment, as an independent, self-directed learning exercise - not a literal
submission of the original assignment.

## Tech stack

| Layer            | Technology                    | Notes                                                                                       |
|-------------------|--------------------------------|-----------------------------------------------------------------------------------------------|
| Frontend          | **React** + TypeScript (Vite) | Layered / Clean-Architecture-inspired structure (`domain` / `infrastructure` / `application` / `presentation`) |
| Backend           | **Java 25 + Spring Boot**     | Domain-Driven Design, layered architecture, CQRS-style command/query services                 |
| Database          | **MySQL**                     | Physical naming strategy auto-generates snake_case, pluralized table/column names             |
| Web server        | **Apache HTTP Server**        | Serves the compiled React build and reverse-proxies `/api/**` to the backend                  |
| Containerization  | **Docker / docker-compose**   | 3 services: `mysql`, `backend`, `apache`                                                      |

Originality/improvements added beyond the base case study:
- Full CRUD (Create/Read/Update/Delete), not just Create.
- Field-level encryption at rest (AES-256-GCM) for `serialNumber`, transparently decrypted in API responses.
- A complete React frontend consuming the API, following the same layered philosophy as the backend.
- Full containerized deployment (Docker Compose + Apache reverse proxy), demonstrating virtualization.

## Project structure

```
caterpillar-mining/
├── caterpillar-mining-backend/    Spring Boot API (see its own README for full endpoint docs)
├── caterpillar-mining-frontend/   React CRUD UI
├── docker-compose.yml             Orchestrates the mysql + backend + apache services
├── .env.example                   Template for the secrets docker-compose needs
└── README.md                      This file
```

## Bounded contexts

`MiningEquipmentUnit` belongs to the `mining` bounded context. The reusable `GeoCoordinate` value
object and cross-cutting infrastructure (encryption, exception handling, auditing, database
naming strategy) live in the `shared` bounded context. See
[`caterpillar-mining-backend/README.md`](caterpillar-mining-backend/README.md) for the full
endpoint reference, request/response examples and business rules.

## Running the project

There are two ways to run it: **Docker Compose** (recommended - runs everything with one command)
or **manually** (useful while developing, with hot reload on both ends).

### Option A - Docker Compose (recommended)

Requires Docker Desktop.

1. Copy the environment template and fill in real values:
   ```bash
   cp .env.example .env
   ```
   - `MYSQL_ROOT_PASSWORD`: any password for the containerized MySQL instance.
   - `MINING_ENCRYPTION_KEY`: a Base64-encoded AES-256 key, e.g. generate one with
     `openssl rand -base64 32`.
2. Build and start everything:
   ```bash
   docker compose up -d --build
   ```
3. Open the app at **http://localhost:8081** (served by Apache). The API is reachable through the
   same origin, at `http://localhost:8081/api/v1/equipment-units` - it is not exposed on its own
   port; every request goes through Apache's reverse proxy.
4. Stop everything with `docker compose down` (add `-v` to also delete the MySQL data volume).

### Option B - Run manually (backend and frontend separately)

Requires Java 25, Node.js 18+, and a local MySQL server.

**Backend**
1. Set the environment variables `DB_PASSWORD` (your local MySQL root password) and
   `MINING_ENCRYPTION_KEY` (a Base64 AES-256 key, e.g. `openssl rand -base64 32`) - required, no
   defaults are hardcoded anywhere in the source. See
   [`caterpillar-mining-backend/README.md`](caterpillar-mining-backend/README.md#running-the-project)
   for exact steps (IntelliJ run configuration or terminal).
2. From `caterpillar-mining-backend/`, run:
   ```bash
   ./mvnw spring-boot:run
   ```
3. The API starts on `http://localhost:8080`; Swagger UI is at
   `http://localhost:8080/swagger-ui/index.html`.

**Frontend**
1. From `caterpillar-mining-frontend/`, install dependencies and start the dev server:
   ```bash
   npm install
   npm run dev
   ```
2. Open `http://localhost:5173`. Vite's dev server proxies `/api/**` requests to
   `http://localhost:8080` (configured in `vite.config.ts`), so the backend from the step above
   must already be running.

## Author

Diego Vilca
