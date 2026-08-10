# Caterpillar Mining Frontend

React + TypeScript (Vite) CRUD UI for the `mining` bounded context's `MiningEquipmentUnit`
resource. See the [repository root README](../README.md) for the full project overview
(what Caterpillar Mining is, the tech stack, and how to run the whole system) and the
[backend README](../caterpillar-mining-backend/README.md) for the API reference.

## Architecture

Layered / Clean-Architecture-inspired structure - each layer only depends on the one "inside" it:

```
src/
├── domain/equipmentUnit/       Framework-agnostic types (EquipmentUnit, OperationStatus, ...)
├── infrastructure/             axios client + the REST calls to the backend
├── application/equipmentUnit/  Hooks orchestrating infrastructure (list state, form state)
├── presentation/
│   ├── components/             Generic, reusable UI (Button, Modal, Banner, ...)
│   └── features/equipmentUnit/ Screen-specific components (table, form, page)
└── shared/                     Cross-cutting utilities (error message extraction)
```

## Running

```bash
npm install
npm run dev
```

Opens on `http://localhost:5173`. The dev server proxies `/api/**` requests to
`http://localhost:8080` (see `vite.config.ts`), so the backend must be running separately -
either via `./mvnw spring-boot:run` in `caterpillar-mining-backend/`, or as part of the full
`docker compose up` stack described in the root README.

## Scripts

- `npm run dev` - start the Vite dev server with hot reload.
- `npm run build` - type-check and produce a production build in `dist/`.
- `npm run lint` - run ESLint.
- `npm run preview` - preview the production build locally.

## Author

Diego Vilca
