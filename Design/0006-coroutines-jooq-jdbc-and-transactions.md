# ADR 0006: Coroutines, jOOQ, JDBC and Transactions

- Status: Accepted
- Datum: 2026-07-07

## Kontext
Repository-Methoden sind `suspend`, aber das JVM-Backend verwendet jOOQ über JDBC. JDBC bleibt blockierend, auch wenn der Aufruf in einer suspendierenden Funktion gekapselt ist.

## Entscheidung
Blocking-Datenbankarbeit läuft in einem zentralen, begrenzten Dispatcher (`Dispatchers.IO.limitedParallelism(16)`) aus `config/CoroutineConfig.kt`. `JooqHelloRepository` wechselt genau an der Adaptergrenze in diesen Dispatcher. Der Code dokumentiert ausdrücklich: `suspend != non-blocking`.

Für Transaktionen gilt: Dispatcher-Wechsel werden nicht beliebig innerhalb imperativer JDBC-Transaktionen verteilt. Spätere atomare fachliche Operationen sollen grobgranulare Server-Kommandos verwenden, z. B. ein einzelnes `transfer()` statt mehrerer HTTP-Aufrufe `debit()`, `credit()`, `createBooking()`.

JS verwendet die natürliche Abbildung `suspend -> fetch -> Promise -> await`. Der Export suspendierender Funktionen nach JS nutzt bewusst `-Xenable-suspend-function-exporting` und ist eine experimentelle Interop-Entscheidung.

## Konsequenzen
### Positive Konsequenzen
- Blocking JDBC wird nicht als non-blocking fehlverkauft.
- Parallelität für Datenbankarbeit ist begrenzt und zentral konfiguriert.
- Die JS-API kann aus TypeScript mit `await` verwendet werden.

### Negative Konsequenzen / Trade-offs
- Ein zusätzlicher Dispatcher ist nötig.
- Suspend-Export an der JS-Grenze bleibt experimentell und muss bei Kotlin-Upgrades geprüft werden.

## Verworfene Alternativen
- Überall `withContext(Dispatchers.IO)`: unkontrolliert und schwer auditierbar.
- Naive Multi-HTTP-Call-Transaktionen aus dem Frontend: keine echte DB-Atomarität.

