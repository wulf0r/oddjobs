# ADR 0001: Monorepo and Build Ownership

- Status: Accepted
- Datum: 2026-07-07

## Kontext
Oddjobs enthält ein Spring/jOOQ-Backend, eine Kotlin-Multiplatform-Shared-Library und ein Vue/Vite-Frontend. Kotlin/JVM, Kotlin/JS, jOOQ-Codegen und Flyway benötigen Gradle, während TypeScript, Vue und Vite im Node-Ökosystem leben.

## Entscheidung
Wir verwenden ein Monorepo mit klar getrennten Build-Welten. Gradle besitzt Backend, Shared-KMP, Flyway, jOOQ-Codegen und das Staging des Kotlin/JS-npm-Artefakts. pnpm besitzt das Frontend und konsumiert das lokal gestagte Paket `@oddjobs/shared` per Workspace.

## Konsequenzen
### Positive Konsequenzen
- Kotlin- und Datenbankartefakte werden zentral über Gradle reproduzierbar gebaut.
- Das Frontend bleibt ein normales Vite/pnpm-Projekt.
- Das npm-Paket bildet eine explizite Grenze zwischen Kotlin/JS und TypeScript.

### Negative Konsequenzen / Trade-offs
- Der Bootstrap umfasst Gradle- und pnpm-Schritte.
- Das gestagte npm-Paket muss nach Änderungen an `shared` neu erzeugt werden.

## Verworfene Alternativen
- Alles hinter Gradle verstecken: reduziert Transparenz für Frontend-Entwicklung.
- Alles über pnpm orchestrieren: passt schlecht zu jOOQ/Flyway/Kotlin-Toolchains.

