# ADR 0005: Platform-specific Repository Adapters

- Status: Accepted
- Datum: 2026-07-07

## Kontext
JVM und Browser haben unterschiedliche I/O-Mechanismen. Das Backend liest PostgreSQL über jOOQ/JDBC; der Browser ruft HTTP per `fetch` auf.

## Entscheidung
Das Backend implementiert den gemeinsamen Port mit `JooqHelloRepository` und generierten jOOQ-Metadaten. JS implementiert denselben Port mit `HttpHelloRepository`, `fetch`, Promise/Coroutine-Interop und zentraler kotlinx-Deserialisierung.

## Konsequenzen
### Positive Konsequenzen
- Die fachliche Schnittstelle bleibt gleich.
- Technische Plattformdetails bleiben in Adaptern gekapselt.
- TypeScript baut nur die kleine `HelloApi`-Fassade auf.

### Negative Konsequenzen / Trade-offs
- Netzwerkfehler im Browser und Datenbankfehler im Backend haben unterschiedliche Semantik.
- Adapter müssen separat getestet und beobachtet werden.

## Verworfene Alternativen
- jOOQ im Shared-Modul: nicht browserfähig und verboten.
- Browser APIs in `commonMain`: verletzt KMP-Plattformneutralität.

