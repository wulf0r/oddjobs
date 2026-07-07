# ADR 0002: Generic Backend Packaging

- Status: Accepted
- Datum: 2026-07-07

## Kontext
Der Hello-World-Slice ist klein, soll aber eine Struktur zeigen, die technische Rollen schnell auffindbar macht. Feature-Packaging wäre bei einem einzelnen Feature künstlich und würde technische Verantwortlichkeiten verdecken.

## Entscheidung
Das Backend wird horizontal nach technischen Rollen paketiert: `controller`, `service`, `repository`, `config`, optional `mapper` und `exception`. `JooqHelloRepository` liegt unter `repository`, `HelloService` unter `service`, `HelloController` unter `controller`.

## Konsequenzen
### Positive Konsequenzen
- Entwickler finden Controller, Services und Repositories ohne Feature-Suche.
- jOOQ- und JDBC-spezifischer Code bleibt klar im Repository-Adapter.
- Die Struktur demonstriert bewusst die technische Schichtung.

### Negative Konsequenzen / Trade-offs
- Bei stark wachsender Domäne kann ergänzendes Feature-Packaging sinnvoll werden.
- Fachliche Zusammengehörigkeit ist weniger direkt sichtbar als bei package-by-feature.

## Verworfene Alternativen
- Primäres package-by-feature: für diesen Auftrag ausdrücklich ausgeschlossen.
- Alles in einem Package: skaliert nicht und verwischt Verantwortlichkeiten.

