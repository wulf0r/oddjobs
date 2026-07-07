# ADR 0006: Coroutines, jOOQ, JDBC and Transactions

- Status: Accepted
- Datum: 2026-07-07

## Kontext
Repository-Methoden sind `suspend`, aber das JVM-Backend verwendet jOOQ über JDBC. JDBC bleibt blockierend, auch wenn der Aufruf in einer suspendierenden Funktion gekapselt ist.

## Entscheidung
`JooqHelloRepository` führt den jOOQ/JDBC-Aufruf direkt aus und dokumentiert ausdrücklich: `suspend != non-blocking`. Das Shared-Repository-Interface bleibt suspendierend, weil die JS-Implementierung echte async/Promise-I/O verwendet und der gemeinsame Port I/O signalisiert. Auf der JVM wird daraus aber kein künstlicher Dispatcher-Wechsel abgeleitet.

Für Transaktionen gilt: Dispatcher-Wechsel werden nicht beliebig innerhalb imperativer JDBC-Transaktionen verteilt. Spätere atomare fachliche Operationen sollen grobgranulare Server-Kommandos verwenden, z. B. ein einzelnes `transfer()` statt mehrerer HTTP-Aufrufe `debit()`, `credit()`, `createBooking()`. Die direkte Ausführung passt besser zu Spring/jOOQ-Transaktionsgrenzen und vermeidet unnötige Coroutine-Infrastruktur im Hello-World-Slice.

JS verwendet die natürliche Abbildung `suspend -> fetch -> Promise -> await`. Der Export suspendierender Funktionen nach JS nutzt bewusst `-Xenable-suspend-function-exporting` und ist eine experimentelle Interop-Entscheidung.

## Konsequenzen
### Positive Konsequenzen
- Blocking JDBC wird nicht als non-blocking fehlverkauft.
- Der Repository-Code bleibt einfach und besser kompatibel mit Spring/jOOQ-Transaktionsgrenzen.
- Die JS-API kann aus TypeScript mit `await` verwendet werden.

### Negative Konsequenzen / Trade-offs
- JVM-seitige JDBC-Arbeit blockiert den aufrufenden Backend-Thread.
- Suspend-Export an der JS-Grenze bleibt experimentell und muss bei Kotlin-Upgrades geprüft werden.

## Verworfene Alternativen
- Ein dedizierter `Dispatchers.IO.limitedParallelism(...)`-Dispatcher für diesen einfachen Read: zu viel Infrastruktur für den aktuellen Slice und potenziell störend für spätere imperative Transaktionen.
- Überall `withContext(Dispatchers.IO)`: unkontrolliert und schwer auditierbar.
- Naive Multi-HTTP-Call-Transaktionen aus dem Frontend: keine echte DB-Atomarität.

