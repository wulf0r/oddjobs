# Oddjobs

Oddjobs is a Kotlin/Spring Boot/jOOQ/Vue/Kotlin-Multiplatform monorepo demonstrating a database-backed Hello World vertical slice.

## Prerequisites

- Docker with Docker Compose for PostgreSQL.
- Node.js `24.18.0` (pinned in `.nvmrc` and `.node-version`).
- pnpm `11.8.0` through Corepack (`corepack prepare pnpm@11.8.0 --activate`).
- Gradle is run through `./gradlew` and pinned to `9.5.0`.
- JDK 25 is requested through Gradle Toolchains with the Foojay resolver.

## Architecture

```text
Vue/TS
   ->
Kotlin/JS HelloApi
   ->
HttpHelloRepository
   ->
REST
   ->
Spring Controller
   ->
Backend HelloService
   ->
JooqHelloRepository
   ->
PostgreSQL
```

Shared contract:

```text
DTO
Repository Interface
Serialization Contract
```

The backend uses generated jOOQ metadata from the migrated PostgreSQL schema. The frontend imports `@example/shared` from the pnpm workspace and does not duplicate DTOs manually.

## Bootstrap

```bash
./gradlew bootstrap
corepack prepare pnpm@11.8.0 --activate
pnpm install --frozen-lockfile
```

`bootstrap` starts PostgreSQL with Docker Compose, runs Flyway, generates jOOQ sources, builds the shared JVM/JS module, and stages the shared npm package under `packages/shared`.

## Development

Start/stop the local DB:

```bash
./gradlew dbUp
./gradlew dbDown
```

Regenerate jOOQ after migration changes:

```bash
./gradlew generateJooq
```

Rebuild and stage the shared npm package:

```bash
./gradlew :shared:stageNpmPackage
```

Start backend:

```bash
./gradlew :backend:bootRun
```

Start frontend:

```bash
pnpm --dir frontend dev
```

Vite proxies `/api` to `http://localhost:8080`.

## Build

```bash
./gradlew clean
./gradlew bootstrap
./gradlew build
pnpm install --frozen-lockfile
pnpm --dir frontend build
```

## Test

```bash
./gradlew test
pnpm --dir frontend type-check
pnpm --dir frontend build
```

The backend integration test uses Testcontainers PostgreSQL. Local end-to-end verification uses the Compose database and the REST endpoint:

```bash
curl http://localhost:8080/api/hello
```

Expected shape:

```json
{
  "message": "Hello World from database — 2026-07-07T12:34:56.123Z"
}
```

## jOOQ Codegen

Flow:

```text
Flyway migration SQL
  -> PostgreSQL schema
  -> jOOQ Kotlin code generation
  -> backend compilation
```

Important paths:

- Migration: `backend/src/main/resources/db/migration/V001__create_hello_message.sql`
- Generated jOOQ sources: `backend/build/generated-src/jooq/main`
- Generated package: `com.oddjobs.backend.generated.jooq`

## Shared npm Package

The Kotlin/JS package is staged by Gradle:

```bash
./gradlew :shared:stageNpmPackage
```

Important paths:

- Staged package metadata: `packages/shared/package.json`
- Staged JS and TypeScript definitions: `packages/shared/dist`

## ADRs

See [Design/README.md](Design/README.md).
