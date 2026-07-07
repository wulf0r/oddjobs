# Design Decisions

Oddjobs records architecture decisions as ADRs in this directory.

| ADR | Title | Summary |
| --- | --- | --- |
| [0001](0001-monorepo-and-build-ownership.md) | Monorepo and Build Ownership | Gradle owns Kotlin, Flyway, jOOQ and npm staging; pnpm owns Vue/Vite. |
| [0002](0002-generic-backend-packaging.md) | Generic Backend Packaging | Backend packages are horizontal technical roles such as controller, service, repository and config. |
| [0003](0003-shared-kmp-api-dtos.md) | Shared KMP API DTOs | DTOs are shared Kotlin types with serialization and generated TypeScript definitions. |
| [0004](0004-shared-suspend-repository-ports.md) | Shared Suspend Repository Ports | Shared repository ports are normal suspend interfaces in commonMain. |
| [0005](0005-platform-specific-repository-adapters.md) | Platform-specific Repository Adapters | JVM uses jOOQ/PostgreSQL and JS uses HTTP/fetch behind the same port. |
| [0006](0006-coroutines-jooq-jdbc-and-transactions.md) | Coroutines, jOOQ, JDBC and Transactions | JDBC remains blocking and uses a bounded dispatcher; JS maps suspend calls to Promise/await. |
| [0007](0007-flyway-database-first-jooq-codegen.md) | Flyway Database-first jOOQ Codegen | Flyway migrations define schema before jOOQ generates Kotlin metadata. |
| [0008](0008-kotlin-js-npm-package-for-vue.md) | Kotlin/JS npm Package for Vue | Kotlin/JS is staged as an ESM npm package with `.d.ts` for Vue. |
| [0009](0009-version-and-toolchain-baseline.md) | Version and Toolchain Baseline | Versions and Java/Node toolchains are pinned to the required baseline. |
