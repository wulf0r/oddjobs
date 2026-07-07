# ADR 0003: Shared KMP API DTOs

- Status: Accepted
- Datum: 2026-07-07

## Kontext
Backend und Frontend verwenden denselben REST-Vertrag. Eine manuell gepflegte TypeScript-Kopie von Kotlin-DTOs würde Contract Drift ermöglichen.

## Entscheidung
API-DTOs liegen in `shared/commonMain`, sind mit `@Serializable` und `@JsExport` annotiert und werden vom Backend, vom Kotlin/JS-Code und über generierte `.d.ts`-Dateien von TypeScript verwendet. JSON Plain Objects aus `response.json()` werden nicht als Kotlin-Instanzen behandelt; der JS-Adapter liest Text und deserialisiert zentral mit `ApiJson`.

## Konsequenzen
### Positive Konsequenzen
- Kein manuell dupliziertes DTO in TypeScript.
- Serialisierungsregeln sind zwischen JVM und JS konsistent.
- Vertragsänderungen werden im Build sichtbar.

### Negative Konsequenzen / Trade-offs
- Frontend und Backend sind compile-time enger gekoppelt.
- Die öffentliche JS-Export-Grenze muss klein und bewusst gehalten werden.
- Kotlin/JS-Interop erfordert Aufmerksamkeit für Plain-Object-vs-Kotlin-Instanz-Semantik.

## Verworfene Alternativen
- Manuelle TypeScript Interfaces: verboten wegen Contract Drift.
- `response.json() as HelloResponse`: nur Casting, keine Deserialisierung.

