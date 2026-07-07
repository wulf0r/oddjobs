# ADR 0004: Shared Suspend Repository Ports

- Status: Accepted
- Datum: 2026-07-07

## Kontext
Der Hello-Slice benötigt dieselbe fachliche Repository-Schnittstelle auf JVM und JS, aber unterschiedliche technische Implementierungen.

## Entscheidung
`HelloRepository` liegt als normales Interface in `shared/commonMain`. I/O-Methoden sind `suspend`. Es gibt kein `expect/actual` für Repository-DI; Implementierungen werden per Konstruktor- oder Framework-DI injiziert. Tests können einfache Fake-Repositories verwenden.

## Konsequenzen
### Positive Konsequenzen
- Ein Port beschreibt den gemeinsamen fachlichen Vertrag.
- JVM und JS können unterschiedliche Adapter verwenden.
- Services sind mit Fake-Repositories deterministisch testbar.

### Negative Konsequenzen / Trade-offs
- `suspend` sagt nichts über die tatsächliche I/O-Technik aus.
- Plattformfehler unterscheiden sich trotz identischem Port.

## Verworfene Alternativen
- `expect/actual` für DI: unnötig und schwerer testbar.
- Spring-Annotationen in `commonMain`: verletzt Plattformneutralität.

