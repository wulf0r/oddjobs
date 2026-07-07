# ADR 0009: Version and Toolchain Baseline

- Status: Accepted
- Datum: 2026-07-07

## Kontext
Oddjobs soll bewusst eine feste, moderne Toolchain-Baseline verwenden. Spring Boot 4.1.0 verwaltet möglicherweise eine ältere Kotlin-Version, während dieses Projekt Kotlin 2.4.0 verlangt.

## Entscheidung
Direkte Versionen werden gepinnt: Kotlin 2.4.0, Gradle Wrapper 9.5.0, Java Toolchain 25, Spring Boot 4.1.0, Foojay Resolver 1.0.0, kotlinx.coroutines 1.10.2, kotlinx.serialization JSON 1.11.0, jOOQ Runtime/Codegen/Gradle Plugin 3.21.5, Flyway 12.4.0, PostgreSQL Docker `postgres:17.10-alpine`, Node 24.18.0, pnpm 11.8.0, Vue 3.5.39, Vite 8.1.3, `@vitejs/plugin-vue` 6.0.7, TypeScript 6.0.3 und `vue-tsc` 3.3.6.

Kotlin Compiler Plugin, Serialization Compiler Plugin, stdlib und reflect werden konsistent mit Kotlin 2.4.0 konfiguriert. Java 25 wird über Gradle Toolchains und Foojay bereitgestellt.

## Konsequenzen
### Positive Konsequenzen
- Reproduzierbare Builds ohne `latest`-Abhängigkeiten.
- Runtime und jOOQ-Codegen verwenden dieselbe Version.
- Toolchain-Erwartungen sind dokumentiert.

### Negative Konsequenzen / Trade-offs
- Lokale Verifikation benötigt Netzwerkzugriff für Gradle/Toolchain/Node-Abhängigkeiten und Docker für PostgreSQL.
- Neue Versionen werden nicht automatisch übernommen.

## Verworfene Alternativen
- Ungepinntes `latest`: nicht reproduzierbar.
- Spring Boots Kotlin-Version übernehmen: verletzt die geforderte Kotlin-2.4.0-Baseline.

