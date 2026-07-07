# ADR 0008: Kotlin/JS npm Package for Vue

- Status: Accepted
- Datum: 2026-07-07

## Kontext
Das Vue-Frontend soll die Kotlin/JS-Shared-Library als echtes npm-Paket konsumieren und generierte TypeScript-Definitionen verwenden.

## Entscheidung
`shared` baut ein ES-Module-Kotlin/JS-Library-Artefakt mit TypeScript-Definitionen. Der Gradle-Task `:shared:stageNpmPackage` kopiert die reale Production-Library-Distribution und `.d.ts`-Dateien nach `packages/shared/dist`. `packages/shared/package.json` exportiert JS und Types als `@example/shared` für den pnpm Workspace.

Die JS-Grenze ist klein: TypeScript verwendet `HelloApi`, nicht interne Repository-Implementierungen. Suspend-Funktionen werden mit `-Xenable-suspend-function-exporting` als Promise-basierte API exportiert.

## Konsequenzen
### Positive Konsequenzen
- Vue konsumiert ein normales Workspace-Paket.
- `.d.ts` vermeidet manuelle DTO-Duplizierung.
- Die öffentliche JS-Oberfläche bleibt bewusst klein.

### Negative Konsequenzen / Trade-offs
- Staging-Pfade müssen bei Kotlin/JS-Änderungen verifiziert werden.
- Das Package muss nach Shared-Änderungen neu gestaged werden.

## Verworfene Alternativen
- Direkte Imports aus `shared/build`: instabil und nicht workspace-freundlich.
- Manuelle TypeScript-Verträge: verboten.

