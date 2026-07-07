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

### One-command Docker Compose dev environment

Build and start the full dev environment:

```bash
docker compose up --build
```

Compose starts:

- PostgreSQL `17.10-alpine` with host port `9993` and data under `./dev/postgre`.
- A dev container with Java `21`, global Gradle `9.5.0`, Node `24.18.0`, and pnpm `11.8.0`.
- Spring Boot on <http://localhost:9991>.
- Vite on <http://localhost:9992>.
- A plain-text combined process log on <http://localhost:9994>.

The Oddjobs project directory is bind-mounted at `/workspace/oddjobs` inside the dev container. On startup the dev container waits for PostgreSQL, runs `pnpm install --frozen-lockfile`, runs Gradle bootstrap with the global `gradle` binary, then starts. The bootstrap task assembles shared artifacts and stages the npm package, but intentionally does not run tests:

- `gradle --no-daemon -Poddjobs.skipDbUp=true :backend:compileKotlin :backend:processResources --continuous`
- `gradle --no-daemon -Poddjobs.skipDbUp=true bootstrap :backend:bootRun`
- `pnpm --dir frontend dev --host 0.0.0.0 --port 9992 --strictPort`

All process output is appended to `/var/log/oddjobs/dev.log` inside the dev container and served as `text/plain` on port `9994`.

Stop the environment:

```bash
docker compose down
```

Remove local database state if you need a clean DB:

```bash
rm -rf dev/postgre
```

Start/stop only the local DB:

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
