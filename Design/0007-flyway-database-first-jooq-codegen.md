# ADR 0007: Flyway Database-first jOOQ Codegen

- Status: Accepted
- Datum: 2026-07-07

## Kontext
Die Datenbankstruktur soll nicht aus Kotlin-Entities abgeleitet werden. jOOQ soll gegen das reale, migrierte PostgreSQL-Schema generieren.

## Entscheidung
Flyway-SQL-Migrationen sind die Source of Truth:

```text
Migration SQL
    ->
DB Schema
    ->
jOOQ Codegen
```

`V001__initial_schema.sql` ersteltl Das basis schema und seedet einen Datensatz. `jooqCodegen` hängt von `flywayMigrate` ab und generiert Kotlin-Artefakte unter `backend/build/generated-src/jooq/main` im Package `com.oddjobs.backend.generated.jooq`.

## Konsequenzen
### Positive Konsequenzen
- Das Repository kompiliert gegen generierte Tabellen-/Record-Metadaten.
- Schemaänderungen laufen über Migrationen.
- String-SQL im Repository wird vermieden.

### Negative Konsequenzen / Trade-offs
- Codegen benötigt eine laufende, migrierte PostgreSQL-Datenbank.
- Bootstrap muss DB-Health, Flyway und jOOQ-Reihenfolge robust orchestrieren.

## Verworfene Alternativen
```text
Entity
    ->
DB
```
Diese Richtung ist für Oddjobs nicht gewünscht.

