# Auftrag: Erstelle ein vollständiges Kotlin/Spring/jOOQ/Vue/Kotlin-MPP-Monorepo

Der Pojektname is Oddjobs. Base packe name sollte com.oddjobs sein.
Das Projekt sollte self contained laufen, z.b. mit einem Befehl innerhalb eines Docker Containers. Sollte dies nicht möglich sein, bitte nachdrücklich darüber informieren.

Du bist ein Senior Staff Software Engineer mit tiefgehender Erfahrung in:

* Kotlin 2.4
* Kotlin Multiplatform
* Kotlin/JVM
* Kotlin/JS
* Spring Boot 4
* Coroutines
* jOOQ
* JDBC
* Flyway
* PostgreSQL
* Gradle 9
* Vue 3
* TypeScript
* Vite
* pnpm Workspaces
* Architekturentscheidungen und ADRs

## Arbeitsmodus

Erstelle im aktuellen Arbeitsverzeichnis ein **vollständig lauffähiges Projekt**.

Wichtig:

* Liefere nicht nur einen Plan.
* Liefere nicht nur Code-Snippets.
* Lege tatsächlich alle benötigten Dateien und Verzeichnisse an.
* Führe Builds und Tests tatsächlich aus.
* Behebe Build-, Compiler-, Typ- und Laufzeitfehler.
* Stoppe nicht beim ersten Fehler.
* Prüfe insbesondere die reale Kotlin/JS-Ausgabestruktur und passe das npm-Staging daran an.
* Erfinde keine Gradle-Tasks oder Kotlin/JS-Output-Pfade.
* Nutze `./gradlew ... tasks` und reale Build-Ausgaben, falls Tasknamen oder Output-Pfade verifiziert werden müssen.
* Das Endergebnis muss reproduzierbar und möglichst self-contained sein.
* Frage nicht nach Bestätigung, sondern treffe vernünftige Entscheidungen innerhalb der folgenden Vorgaben.
* Ändere die festgelegten Architekturentscheidungen nicht stillschweigend.
* Falls eine technische Einschränkung eine minimale Abweichung erzwingt, dokumentiere sie in einem ADR und im Abschlussbericht.

---

# 1. Ziel

Erstelle ein Monorepo mit:

1. Kotlin/Spring-Boot-Backend
2. jOOQ-basierter Persistenz
3. PostgreSQL-Datenbank
4. Flyway-Migrationen als Schema-Source-of-Truth
5. Kotlin-Multiplatform-Shared-Library
6. JVM- und JS-Target der Shared Library
7. gemeinsam genutzten DTOs
8. gemeinsam genutzten Repository-Interfaces
9. `suspend` Repository-Methoden
10. JVM-spezifischer jOOQ-Repository-Implementierung
11. JS-spezifischer HTTP-Repository-Implementierung
12. TypeScript/Vue-Frontend
13. npm-Consumption der Kotlin/JS-Library
14. automatisch generierten TypeScript-Definitionen
15. einer vollständigen „Hello World“-Vertikalscheibe
16. Architekturentscheidungen als ADRs im Root-Verzeichnis `Design/`

Das Projekt soll nicht nur kompilieren, sondern die Architektur praktisch demonstrieren.

---

# 2. Verbindliche Versionsmatrix

Verwende exakt folgende Baseline-Versionen, sofern nicht ausdrücklich anders angegeben:

## JVM / Kotlin / Build

* Kotlin: `2.4.0`
* Gradle Wrapper: `9.5.0`
* Java/JDK Toolchain: `25`
* Spring Boot: `4.1.0`
* Foojay Toolchain Resolver Convention Plugin: `1.0.0`

## Shared / Coroutines / Serialization

* kotlinx.coroutines: `1.10.2`
* kotlinx.serialization JSON Runtime: `1.11.0`

## Persistence

* jOOQ Runtime: `3.21.5`
* jOOQ Code Generator: `3.21.5`
* jOOQ Gradle Codegen Plugin: `3.21.5`
* Flyway: `12.4.0`
* PostgreSQL Docker Image: `postgres:17.10-alpine`

Runtime und Code Generator von jOOQ müssen dieselbe Version verwenden.

## Frontend

* Node.js: `24.18.0`
* pnpm: `11.8.0`
* Vue: `3.5.39`
* Vite: `8.1.3`
* `@vitejs/plugin-vue`: `6.0.7`
* TypeScript: `6.0.3`
* `vue-tsc`: `3.3.6`

Verwende keine Beta-, RC- oder Snapshot-Versionen.

Direkte Dependencies sollen exakt gepinnt werden, nicht mit unkontrollierten `latest`-Versionen.

Spring-interne und sonstige transitive Dependencies sollen nach Möglichkeit durch Spring Boot 4.1.0 verwaltet werden. Überschreibe keine Spring-Framework-Versionen manuell.

Beachte ausdrücklich:

* Spring Boot 4.1.0 verwaltet standardmäßig eine ältere Kotlin-Version als 2.4.0.
* Dieses Projekt soll trotzdem bewusst Kotlin `2.4.0` verwenden.
* Stelle sicher, dass Kotlin Compiler Plugin, Kotlin Serialization Compiler Plugin, Kotlin stdlib und Kotlin reflect konsistent mit Kotlin `2.4.0` aufgelöst werden.
* Verifiziere die tatsächlich aufgelösten Dependency-Versionen.

---

# 3. Erwartete Repository-Struktur

Erzeuge mindestens folgende Struktur:

```text
.
+-- Design/
¦   +-- README.md
¦   +-- 0001-monorepo-and-build-ownership.md
¦   +-- 0002-generic-backend-packaging.md
¦   +-- 0003-shared-kmp-api-dtos.md
¦   +-- 0004-shared-suspend-repository-ports.md
¦   +-- 0005-platform-specific-repository-adapters.md
¦   +-- 0006-coroutines-jooq-jdbc-and-transactions.md
¦   +-- 0007-flyway-database-first-jooq-codegen.md
¦   +-- 0008-kotlin-js-npm-package-for-vue.md
¦   +-- 0009-version-and-toolchain-baseline.md
¦
+-- backend/
¦   +-- build.gradle.kts
¦   +-- src/
¦       +-- main/
¦       ¦   +-- kotlin/
¦       ¦   +-- resources/
¦       ¦       +-- db/migration/
¦       +-- test/
¦
+-- shared/
¦   +-- build.gradle.kts
¦   +-- src/
¦       +-- commonMain/kotlin/
¦       +-- commonTest/kotlin/
¦       +-- jvmMain/kotlin/
¦       +-- jvmTest/kotlin/
¦       +-- jsMain/kotlin/
¦       +-- jsTest/kotlin/
¦
+-- frontend/
¦   +-- package.json
¦   +-- tsconfig.json
¦   +-- vite.config.ts
¦   +-- index.html
¦   +-- src/
¦
+-- packages/
¦   +-- shared/
¦       +-- package.json
¦       +-- dist/
¦
+-- gradle/
¦   +-- libs.versions.toml
¦   +-- wrapper/
¦
+-- settings.gradle.kts
+-- build.gradle.kts
+-- gradle.properties
+-- gradlew
+-- gradlew.bat
+-- docker-compose.yml
+-- package.json
+-- pnpm-workspace.yaml
+-- pnpm-lock.yaml
+-- .nvmrc
+-- .node-version
+-- .gitignore
+-- README.md
```

Passe Details an reale Tool-Ausgaben an, aber behalte die grundlegende Modulstruktur:

* `backend`
* `shared`
* `frontend`
* `packages/shared`
* `Design`

---

# 4. Zentrale Architekturentscheidungen

## 4.1 Monorepo mit zwei Build-Welten

Gradle besitzt:

* Backend
* Kotlin Multiplatform Shared Library
* JVM Build
* Kotlin/JS Build
* jOOQ Codegen
* Flyway-Codegen-Vorbereitung

pnpm/Vite besitzt:

* Vue-Frontend
* TypeScript-Build
* Workspace-Consumption des Shared-npm-Pakets

Die Build-Welten sollen klar getrennt bleiben.

Gradle darf Kotlin bauen und das npm-Artefakt stagen.

pnpm darf das Frontend bauen und das lokale Shared-Paket konsumieren.

Keine unnötige vollständige Versteckung von pnpm hinter Gradle.

---

## 4.2 Backend muss generisch/horizontal paketiert werden

Das Backend darf ausdrücklich **nicht primär package-by-feature** strukturiert werden.

Verwende eine technisch generische, gut auffindbare Paketstruktur wie:

```text
com.example.helloworld
+-- controller/
+-- service/
+-- repository/
+-- mapper/
+-- config/
+-- exception/
+-- Application.kt
```

Für den Hello-World-Use-Case beispielsweise:

```text
controller/
    HelloController.kt

service/
    HelloService.kt

repository/
    JooqHelloRepository.kt

config/
    CoroutineConfig.kt
    TimeConfig.kt
```

Begründung:

* Entwickler sollen technische Rollen schnell finden.
* Repositories gehören nach `repository`.
* Services gehören nach `service`.
* Controller gehören nach `controller`.

Dokumentiere diese bewusste Entscheidung gegen primäres Feature-Packaging in ADR 0002.

---

## 4.3 DTOs werden zwischen Backend und Frontend geteilt

DTOs dürfen nicht manuell in Kotlin und TypeScript dupliziert werden.

Es darf insbesondere **keine manuell gepflegte TypeScript-Kopie** eines Kotlin-API-DTOs geben.

Beispiel:

```kotlin
@Serializable
@JsExport
data class HelloResponse(
    val message: String,
)
```

Dieser Typ soll:

* im Backend verwendet werden
* als REST Response verwendet werden
* in Kotlin/JS verwendet werden
* TypeScript-seitig über generierte `.d.ts` sichtbar sein

Verwende:

* `kotlinx.serialization`
* `@Serializable`
* `@JsExport`
* generierte TypeScript Definitions

Dokumentiere in ADR 0003:

* warum DTO-Sharing gewählt wurde
* Vermeidung von Contract-Drift
* Vermeidung manueller Duplizierung
* Compile-Time-Kopplung als bewusst akzeptierte Konsequenz

---

## 4.4 JSON ist nicht automatisch eine Kotlin-Klasseninstanz

Berücksichtige ausdrücklich:

Ein Ergebnis von:

```typescript
await response.json()
```

ist ein JavaScript Plain Object und nicht automatisch eine Runtime-Instanz einer exportierten Kotlin-Klasse.

Löse dieses Problem nicht durch DTO-Duplizierung.

Löse es stattdessen durch zentrale Deserialisierung in Kotlin/JS.

Die JS-spezifische HTTP-Repository-Implementierung soll:

1. HTTP aufrufen
2. Response als Text lesen
3. mit `kotlinx.serialization` in das gemeinsame DTO dekodieren

Beispielkonzept:

```kotlin
val dto = ApiJson.decodeFromString<HelloResponse>(
    response.text().await()
)
```

Definiere nach Möglichkeit eine gemeinsame JSON-Konfiguration im Shared-Modul, beispielsweise:

```kotlin
object ApiJson {
    val instance = Json {
        ignoreUnknownKeys = false
        encodeDefaults = true
        explicitNulls = true
    }
}
```

Verwende dieselbe oder semantisch identische Konfiguration auf JVM und JS.

Das Backend soll Spring Boot 4.1.0 mit Kotlinx Serialization verwenden, vorzugsweise über:

```text
spring-boot-starter-kotlinx-serialization-json
```

Stelle durch Tests sicher, dass dieselben Shared DTOs korrekt als REST JSON serialisiert und im JS-Target wieder deserialisiert werden können.

---

# 5. Shared Kotlin Multiplatform Modul

Das Modul `shared` muss mindestens Targets für:

* JVM
* JavaScript Browser

enthalten.

## 5.1 Kotlin-MPP-Konfiguration

Verwende Kotlin `2.4.0`.

Konfiguriere:

* JVM Target
* JS Browser Target
* ES Modules
* TypeScript Definition Generation
* Java 25 JVM Toolchain

Sinngemäß:

```kotlin
kotlin {
    jvmToolchain(25)

    jvm {
        // JVM target 25
    }

    js {
        browser()
        useEsModules()
        generateTypeScriptDefinitions()
    }
}
```

Verwende reale Kotlin-2.4-DSL-Syntax.

Konfiguriere für Kotlin/JS ausdrücklich:

```text
-Xenable-suspend-function-exporting
```

weil suspendierende Funktionen an der JS-/TS-Grenze als async/Promise konsumiert werden sollen.

Behandle den Suspend-Export als bewusst experimentelle Interop-Entscheidung und dokumentiere dies in ADR 0006 oder ADR 0008.

---

## 5.2 Shared-Package-Struktur

Verwende im `commonMain` eine generische technische Struktur:

```text
com.example.shared
+-- dto/
+-- repository/
+-- serialization/
```

Optional darf zusätzlich gemeinsame, wirklich plattformneutrale Business-Logik unter:

```text
service/
validation/
domain/
```

liegen.

Füge aber keinen gemeinsamen Service nur aus architektonischer Symmetrie hinzu.

Shared bedeutet:

> fachlich oder vertraglich wirklich identisch auf JVM und JS

Nicht:

> jede beliebige Kotlin-Klasse wird in den Browser exportiert

---

# 6. Shared Repository Interface

Lege das Repository-Interface in `shared/commonMain` ab.

Beispiel:

```kotlin
interface HelloRepository {
    suspend fun getHello(): HelloResponse
}
```

Vorgaben:

* normales Kotlin Interface
* kein Spring
* kein jOOQ
* kein JDBC
* kein Browser API
* keine `expect/actual`-Lösung
* Dependency Injection über normale Konstruktoren
* Methoden für I/O standardmäßig `suspend`

Das Interface ist ein gemeinsamer Port.

Dokumentiere diese Entscheidung in ADR 0004.

---

# 7. Plattformabhängige Repository-Implementierungen

## 7.1 JVM

Im Backend:

```text
backend/.../repository/JooqHelloRepository.kt
```

Implementiert:

```kotlin
HelloRepository
```

Technologie:

* jOOQ
* JDBC
* PostgreSQL
* generierte jOOQ-Artefakte

## 7.2 JavaScript

Im JS Source Set:

```text
shared/src/jsMain/.../repository/HttpHelloRepository.kt
```

Implementiert ebenfalls:

```kotlin
HelloRepository
```

Technologie:

* Browser `fetch`
* Promise/Coroutine Interop
* `await()`
* Shared Kotlinx Serialization
* Shared DTOs

Der HTTP-Adapter ruft das Spring-Backend auf.

Dokumentiere diese Adapterentscheidung in ADR 0005.

---

# 8. Async- und Coroutine-Architektur

Repository-Methoden sollen standardmäßig `suspend` sein.

Beispiel:

```kotlin
interface HelloRepository {
    suspend fun getHello(): HelloResponse
}
```

Dokumentiere und implementiere folgende Regeln:

## 8.1 `suspend` bedeutet nicht automatisch non-blocking

Ein `suspend fun` macht JDBC nicht non-blocking.

jOOQ über JDBC bleibt blockierend.

## 8.2 JS-Seite

Auf JS-Seite ist die Abbildung natürlich:

```text
suspend
    ?
fetch
    ?
Promise
    ?
await
```

Die exportierte JS-Fassade soll aus TypeScript ungefähr so konsumierbar sein:

```typescript
const response = await helloApi.loadHello()
```

Die generierte `.d.ts`-Signatur soll Promise-basiert sein.

Prüfe die tatsächlich generierte Signatur.

## 8.3 JVM-Seite

JDBC-/jOOQ-Aufrufe dürfen nicht fälschlich als non-blocking dargestellt werden.

Verwende für den einfachen Hello-World-Read einen bewusst begrenzten Blocking-I/O-Kontext.

Beispielkonzept:

```kotlin
val dbDispatcher =
    Dispatchers.IO.limitedParallelism(16)
```

Die konkrete Parallelität darf sinnvoll gewählt werden, muss aber dokumentiert sein.

Jooq Repository sinngemäß:

```kotlin
override suspend fun getHello(): HelloResponse =
    withContext(dbDispatcher) {
        // blocking jOOQ/JDBC query
    }
```

Konfiguriere den Dispatcher zentral unter `config/`.

## 8.4 Transaktionsregel

Dokumentiere ausdrücklich:

* nicht blind überall `withContext(Dispatchers.IO)` verteilen
* nicht unkontrolliert Dispatcher wechseln, während eine imperative JDBC-Transaktion aktiv ist
* komplexe atomare Operationen auf grober fachlicher Ebene modellieren

Beispiel für spätere komplexe Operationen:

Nicht:

```text
frontend:
  debit()
  credit()
  createBooking()
```

Sondern:

```text
frontend:
  transfer()
      ?
ein HTTP Request
      ?
backend:
  eine DB Transaction
```

Für das Hello-World-Beispiel ist nur ein einfacher Read erforderlich.

Dokumentiere diese Entscheidung ausführlich in:

```text
Design/0006-coroutines-jooq-jdbc-and-transactions.md
```

---

# 9. Java-25-Toolchain

Konfiguriere Java 25 vollständig.

## 9.1 Foojay Toolchain Resolver

In `settings.gradle.kts`:

```kotlin
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}
```

## 9.2 JVM Toolchain

Für JVM-Module:

```kotlin
java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}
```

Für Kotlin:

```kotlin
kotlin {
    jvmToolchain(25)
}
```

Setze den Kotlin JVM Target konsistent auf Java 25.

Stelle sicher, dass:

* Compile Tasks
* Test Tasks
* Spring Boot Run
* Shared JVM Target

konsistent Java 25 verwenden.

Konfiguriere nach Möglichkeit zusätzlich die Gradle Daemon JVM Criteria für Java 25, sofern dies mit Gradle 9.5 sauber möglich ist.

Verifiziere mit realen Tasks und Build-Ausgaben.

---

# 10. Gradle

Verwende:

```text
Gradle Wrapper 9.5.0
```

Erzeuge vollständig:

```text
gradlew
gradlew.bat
gradle/wrapper/gradle-wrapper.jar
gradle/wrapper/gradle-wrapper.properties
```

Die Wrapper-Konfiguration muss exakt auf Gradle 9.5.0 zeigen.

Verwende Kotlin DSL:

```text
build.gradle.kts
settings.gradle.kts
```

Verwende:

```text
gradle/libs.versions.toml
```

als zentrale Version Catalog Datei.

Keine unnötige Versionsduplizierung über verschiedene Build-Dateien.

---

# 11. Spring Boot Backend

Verwende Spring Boot `4.1.0`.

Mindestens benötigte Backend-Funktionalität:

* Spring MVC
* Kotlin
* suspendierender Controller
* jOOQ
* PostgreSQL JDBC
* Flyway
* Kotlinx Serialization
* Coroutines Integration

Bevorzugte Spring Boot Starter:

```text
spring-boot-starter-webmvc
spring-boot-starter-jooq
spring-boot-starter-flyway
spring-boot-starter-kotlinx-serialization-json
spring-boot-starter-test
```

Ergänze:

```text
kotlin-reflect
kotlinx-coroutines-reactor
PostgreSQL JDBC driver
```

Verwende Spring Boot Dependency Management/BOM sinnvoll.

Verifiziere insbesondere, dass Kotlin `2.4.0` nicht versehentlich durch Boot auf `2.3.x` zurückgezogen wird.

---

# 12. Hello-World-Datenbank

Verwende PostgreSQL:

```text
postgres:17.10-alpine
```

Erzeuge ein `docker-compose.yml`.

Konfiguriere mindestens:

* Datenbankname: `helloworld`
* User: `helloworld`
* Passwort: `helloworld`
* Port: `5432`
* Healthcheck

Nutze für lokale Entwicklung nachvollziehbare Default-Werte.

Keine Produktionsgeheimnisse vortäuschen.

---

# 13. Flyway Migration als Schema Source of Truth

Lege mindestens an:

```text
backend/src/main/resources/db/migration/V001__create_hello_message.sql
```

Migration:

1. Tabelle `hello_message` erstellen
2. mindestens einen Datensatz einfügen

Beispiel-Schema:

```sql
create table hello_message (
    id bigint generated always as identity primary key,
    message varchar(255) not null
);
```

Seed:

```sql
insert into hello_message (message)
values ('Hello World from database');
```

Flyway-Migrationen sind die Source of Truth.

Nicht:

```text
Kotlin Entity
    ?
Schema
```

Sondern:

```text
Flyway SQL
    ?
PostgreSQL Schema
    ?
jOOQ Code Generation
```

---

# 14. jOOQ Code Generation

Verwende den offiziellen jOOQ Gradle Code Generator.

Version:

```text
3.21.5
```

Runtime:

```text
3.21.5
```

Codegen:

```text
3.21.5
```

Keine Versionsabweichung.

## 14.1 Codegen-Pipeline

Erzeuge eine nachvollziehbare Pipeline:

```text
dbUp
    ?
flywayMigrate
    ?
jooqCodegen
    ?
compileKotlin
```

oder eine technisch gleichwertige robuste Lösung.

Die jOOQ-Artefakte müssen aus dem tatsächlich migrierten PostgreSQL-Schema generiert werden.

Keine handgeschriebenen Fake-jOOQ-Klassen.

## 14.2 Generator

Bevorzuge den Kotlin Generator:

```text
org.jooq.codegen.KotlinGenerator
```

Ziel-Package beispielsweise:

```text
com.example.helloworld.generated.jooq
```

Generierter Source-Pfad beispielsweise:

```text
backend/build/generated-src/jooq/main
```

Passe den Pfad an die reale Gradle-Konfiguration an.

Generiere mindestens:

* Tables
* Records

Keine unnötigen DAOs oder POJOs, sofern sie für das Beispiel nicht gebraucht werden.

## 14.3 Backend-Nutzung

`JooqHelloRepository` muss tatsächlich auf die generierten Artefakte zugreifen.

Beispielsweise konzeptionell:

```kotlin
HELLO_MESSAGE.MESSAGE
```

Die Anwendung gilt nicht als fertig, wenn das Repository mit String-SQL statt mit den generierten jOOQ-Metadaten arbeitet.

---

# 15. Backend Hello Repository

Implementiere:

```text
repository/JooqHelloRepository.kt
```

Es implementiert das Shared Interface:

```kotlin
HelloRepository
```

Sinngemäß:

```kotlin
@Repository
class JooqHelloRepository(
    private val dsl: DSLContext,
    private val dbDispatcher: CoroutineDispatcher,
) : HelloRepository {

    override suspend fun getHello(): HelloResponse =
        withContext(dbDispatcher) {
            val message = dsl
                .select(HELLO_MESSAGE.MESSAGE)
                .from(HELLO_MESSAGE)
                .orderBy(HELLO_MESSAGE.ID.asc())
                .limit(1)
                .fetchOne(HELLO_MESSAGE.MESSAGE)
                ?: error("No hello message configured")

            HelloResponse(
                message = message
            )
        }
}
```

Nutze reale generierte jOOQ-Klassen.

jOOQ Records dürfen die Repository-Grenze nicht verlassen.

---

# 16. Backend Hello Service

Implementiere unter:

```text
service/HelloService.kt
```

Der Service:

1. ruft das Shared `HelloRepository` auf
2. liest damit die DB-basierte Nachricht
3. macht die Antwort dynamisch
4. fügt beispielsweise einen serverseitigen UTC-Zeitstempel hinzu

Beispielresultat:

```text
Hello World from database — 2026-07-07T12:34:56.123Z
```

Verwende einen injizierbaren:

```text
java.time.Clock
```

statt `Instant.now()` hart zu verdrahten.

Beispielkonzept:

```kotlin
@Service
class HelloService(
    private val repository: HelloRepository,
    private val clock: Clock,
) {
    suspend fun getHello(): HelloResponse {
        val base = repository.getHello()

        return base.copy(
            message = "${base.message} — ${Instant.now(clock)}"
        )
    }
}
```

Dadurch muss der Service deterministisch testbar sein.

---

# 17. REST Endpoint

Implementiere:

```text
GET /api/hello
```

Controller unter:

```text
controller/HelloController.kt
```

Suspendierend:

```kotlin
@RestController
@RequestMapping("/api/hello")
class HelloController(
    private val helloService: HelloService,
) {
    @GetMapping
    suspend fun getHello(): HelloResponse =
        helloService.getHello()
}
```

Die Response verwendet das Shared DTO.

Erwartetes JSON:

```json
{
  "message": "Hello World from database — 2026-07-07T12:34:56.123Z"
}
```

Der Timestamp muss dynamisch sein.

---

# 18. JS HTTP Repository

Implementiere unter `shared/src/jsMain`:

```text
repository/HttpHelloRepository.kt
```

Es implementiert:

```kotlin
HelloRepository
```

Vorgaben:

* Browser `fetch`
* HTTP GET
* suspend
* Promise `await()`
* Fehler bei non-2xx
* Response Text lesen
* mit Shared `ApiJson` dekodieren
* kein TypeScript-Duplicate-DTO

Sinngemäß:

```kotlin
class HttpHelloRepository(
    private val baseUrl: String,
) : HelloRepository {

    override suspend fun getHello(): HelloResponse {
        val response = window
            .fetch("$baseUrl/hello")
            .await()

        if (!response.ok) {
            error(
                "GET $baseUrl/hello failed with HTTP ${response.status}"
            )
        }

        return ApiJson.instance.decodeFromString(
            response.text().await()
        )
    }
}
```

Passe URL-Komposition und APIs sauber an.

---

# 19. Exportierte JS-/TypeScript-Fassade

Exportiere eine kleine, bewusst JS-freundliche Fassade.

Beispielsweise:

```kotlin
@JsExport
class HelloApi(
    baseUrl: String,
) {
    private val repository =
        HttpHelloRepository(baseUrl)

    suspend fun loadHello(): HelloResponse =
        repository.getHello()
}
```

Oder eine gleichwertige Factory-Lösung.

Wichtig:

* TypeScript soll keinen Kotlin-internen Dependency-Injection-Graph bauen müssen.
* Repository-Implementierung bleibt verborgen.
* Die öffentliche JS-Grenze bleibt klein.
* `loadHello()` soll TypeScript-seitig als Promise konsumierbar sein.

Erwartete Nutzung:

```typescript
const helloApi = new HelloApi('/api')

const response = await helloApi.loadHello()
```

Verifiziere die real generierte `.d.ts`.

Nicht nur annehmen.

---

# 20. Kotlin/JS als npm-Paket

Die Shared Library muss als echtes lokales npm-Paket konsumiert werden.

Package Name:

```text
@example/shared
```

## 20.1 Workspace

Root:

```yaml
packages:
  - "frontend"
  - "packages/*"
```

Frontend:

```json
{
  "dependencies": {
    "@example/shared": "workspace:*"
  }
}
```

## 20.2 Staging

Kotlin/JS soll zunächst den realen JS-/TypeScript-Output erzeugen.

Danach soll ein Gradle Task, beispielsweise:

```text
:shared:stageNpmPackage
```

den benötigten Output stabil nach:

```text
packages/shared/dist
```

stagen.

Wichtig:

* Ermittle den tatsächlichen Kotlin-2.4-Output-Pfad.
* Hardcode keinen Pfad aus alten Tutorials, ohne ihn zu verifizieren.
* Nutze reale Gradle Tasks.
* Sorge dafür, dass JS und `.d.ts` gemeinsam gestaged werden.
* Stelle sicher, dass `packages/shared/package.json` gültige ESM-Exports und Types-Einträge enthält.

Beispielkonzept:

```json
{
  "name": "@example/shared",
  "version": "0.0.0-local",
  "private": true,
  "type": "module",
  "exports": {
    ".": {
      "types": "./dist/...",
      "import": "./dist/..."
    }
  }
}
```

Passe die Dateinamen an den real erzeugten Output an.

Der Frontend-Build muss beweisen, dass das Package korrekt ist.

---

# 21. Vue Frontend

Erstelle ein minimales Vue-3-TypeScript-Frontend.

Versionen exakt gemäß Versionsmatrix.

Kein unnötiger Router.

Kein unnötiges Pinia.

Keine UI-Library.

## 21.1 Hello-World-Seite

Die Startseite muss:

1. initial einen Loading-Zustand anzeigen
2. die Kotlin/JS Shared Library importieren
3. die exportierte `HelloApi` verwenden
4. das Backend aufrufen
5. die dynamische Nachricht anzeigen
6. einen Error-Zustand darstellen
7. optional einen „Refresh“-Button anbieten

Beispielkonzept:

```typescript
import { HelloApi } from '@example/shared'

const api = new HelloApi('/api')

const response = await api.loadHello()
```

Wichtig:

Es darf keine manuell duplizierte Definition wie diese geben:

```typescript
interface HelloResponse {
  message: string
}
```

Der Typ muss aus dem generierten Kotlin/JS-Paket stammen.

---

# 22. Vite Development Proxy

Konfiguriere Vite so, dass:

```text
/api
```

im Development Mode auf:

```text
http://localhost:8080
```

proxied wird.

Damit soll lokal keine unnötige CORS-Konfiguration erforderlich sein.

Der Browser ruft auf:

```text
/api/hello
```

Vite proxied auf:

```text
http://localhost:8080/api/hello
```

---

# 23. Root pnpm Workspace

Erzeuge Root `package.json`.

Mindestens:

```json
{
  "private": true,
  "packageManager": "pnpm@11.8.0",
  "engines": {
    "node": "24.18.0"
  }
}
```

Füge sinnvolle Scripts hinzu, beispielsweise:

```text
frontend:dev
frontend:build
shared:stage
build
```

Verwende keine unkontrollierten globalen npm-Installationen.

Erzeuge:

```text
.nvmrc
.node-version
```

jeweils mit:

```text
24.18.0
```

---

# 24. Self-contained Developer Experience

Das Projekt soll möglichst self-contained sein.

Mindestens:

* Gradle Wrapper enthalten
* Gradle 9.5.0 gepinnt
* Java 25 Toolchain konfiguriert
* Foojay Toolchain Auto-Provisioning
* Node-Version gepinnt
* pnpm-Version gepinnt
* PostgreSQL über Docker Compose
* DB Credentials mit lokalen Defaults
* keine externe manuelle DB-Vorbereitung
* Flyway Migrationen im Repository
* jOOQ Codegen im Build
* Shared npm Package Build im Repository
* README mit exakten Befehlen

Erzeuge sinnvolle Gradle Aggregate Tasks, beispielsweise:

```text
dbUp
dbDown
generateJooq
stageSharedNpmPackage
bootstrap
```

Die exakten Tasknamen dürfen angepasst werden.

Wichtig ist die Funktionalität.

Ein sinnvoller Bootstrap Flow soll dokumentiert sein.

Beispielsweise:

```bash
./gradlew bootstrap
pnpm install --frozen-lockfile
```

oder eine gleichwertige robuste Sequenz.

Der Bootstrap muss:

1. PostgreSQL starten bzw. sicherstellen
2. auf DB Health warten
3. Flyway ausführen
4. jOOQ generieren
5. Shared JVM/JS kompilieren
6. npm-Paket stagen

Wenn der Agent eine bessere robuste Reihenfolge implementiert, ist das erlaubt.

---

# 25. Build-Abhängigkeiten und Docker

Vermeide Race Conditions.

`docker compose up -d` allein reicht nicht, wenn anschließend sofort Flyway startet.

Nutze:

* Docker Healthcheck
* `docker compose up --wait`
* oder eine gleichwertige robuste Wait-Strategie

jOOQ Codegen darf nicht gegen ein noch nicht migriertes Schema laufen.

Die Reihenfolge muss garantiert sein.

---

# 26. Tests

Implementiere sinnvolle Tests.

## 26.1 Shared

Mindestens:

* JSON Serialization Roundtrip für `HelloResponse`
* gemeinsame DTO-Verwendung

## 26.2 Backend Service

Teste `HelloService` mit:

* Fake `HelloRepository`
* Fixed `Clock`

Erwartete Nachricht deterministisch prüfen.

## 26.3 Backend Integration

Mindestens ein Integrationstest soll prüfen:

```text
GET /api/hello
```

und sicherstellen:

* HTTP 200
* JSON hat `message`
* DB-basierter Basisteil enthalten
* dynamischer Teil enthalten

Nutze bei Bedarf Testcontainers für PostgreSQL.

Verwende von Spring Boot verwaltete kompatible Test-Dependencies.

## 26.4 jOOQ

Stelle durch Build oder Integrationstest sicher:

* generierte Tabellenklasse existiert
* Repository kompiliert gegen generierte jOOQ-Artefakte
* Query liest tatsächlich `hello_message`

## 26.5 Frontend

Mindestens:

* TypeScript Type Check
* Vue Type Check
* Production Build

Diese Commands müssen erfolgreich sein:

```bash
pnpm --dir frontend type-check
pnpm --dir frontend build
```

oder gleichwertige Workspace-Commands.

---

# 27. ADRs

Alle Architekturentscheidungen müssen unmittelbar im Root-Verzeichnis:

```text
Design/
```

dokumentiert werden.

Verwende für jedes ADR mindestens:

```text
# ADR XXXX: Titel

- Status: Accepted
- Datum: 2026-07-07

## Kontext

## Entscheidung

## Konsequenzen

### Positive Konsequenzen

### Negative Konsequenzen / Trade-offs

## Verworfene Alternativen
```

## ADR 0001: Monorepo and Build Ownership

Dokumentiere:

* Monorepo
* Gradle besitzt Kotlin
* pnpm besitzt Frontend
* staged npm package
* klare Build-Grenzen

## ADR 0002: Generic Backend Packaging

Dokumentiere:

* `controller`
* `service`
* `repository`
* `mapper`
* `config`
* bewusste Entscheidung gegen primäres package-by-feature

## ADR 0003: Shared KMP API DTOs

Dokumentiere:

* DTOs shared
* `@Serializable`
* `@JsExport`
* keine manuelle TS-Duplizierung
* Vermeidung von Contract Drift
* JSON Plain Objects als bekanntes Interop-Thema
* Lösung durch zentrale Deserialisierung

## ADR 0004: Shared Suspend Repository Ports

Dokumentiere:

* Repository Interface in commonMain
* normale Interfaces
* `suspend`
* kein `expect/actual`
* DI
* Fake-Repositories für Tests

## ADR 0005: Platform-specific Repository Adapters

Dokumentiere:

* JVM: jOOQ/PostgreSQL
* JS: HTTP/fetch
* dasselbe Port-Interface
* unterschiedliche technische Semantik
* Netzwerkfehler vs. DB-Fehler als bewusster Trade-off

## ADR 0006: Coroutines, jOOQ, JDBC and Transactions

Dokumentiere ausführlich:

* `suspend != non-blocking`
* JDBC blockiert
* begrenzter DB Dispatcher
* JS Promise Interop
* experimenteller Suspend Export
* Transaktionsgrenzen
* keine naiven Multi-HTTP-Call-Transaktionen
* coarse-grained server-side commands für atomare Operationen

## ADR 0007: Flyway Database-first jOOQ Codegen

Dokumentiere:

```text
Migration SQL
    ?
DB Schema
    ?
jOOQ Codegen
```

Nicht:

```text
Entity
    ?
DB
```

## ADR 0008: Kotlin/JS npm Package for Vue

Dokumentiere:

* ESM
* npm package
* `.d.ts`
* pnpm workspace
* staging
* kleine JS Export Boundary

## ADR 0009: Version and Toolchain Baseline

Dokumentiere:

* Kotlin 2.4.0
* Gradle 9.5.0
* Java 25
* Spring Boot 4.1.0
* jOOQ 3.21.5
* Frontend-Versionen
* warum Versionen bewusst gepinnt sind

Erzeuge zusätzlich:

```text
Design/README.md
```

mit Index und Kurzbeschreibung aller ADRs.

---

# 28. Architekturregeln, die nicht verletzt werden dürfen

## Verboten: DTO-Duplizierung

Nicht:

```text
shared/HelloResponse.kt

und zusätzlich

frontend/src/types/HelloResponse.ts
```

## Verboten: jOOQ Record über Repository-Grenze

Nicht:

```kotlin
suspend fun getHello(): HelloMessageRecord
```

## Verboten: Spring in commonMain

Nicht:

```kotlin
@Service
class SharedService
```

## Verboten: jOOQ in commonMain

## Verboten: Browser APIs in commonMain

## Verboten: `expect/actual` nur für Repository-DI

Verwende normale Interfaces.

## Verboten: `suspend` als Behauptung von Non-Blocking-I/O

Dokumentiere JDBC korrekt als blocking.

## Verboten: manuelles TS-Casting als Deserialisierung

Nicht:

```typescript
const dto =
  await response.json() as HelloResponse
```

## Verboten: String-SQL statt generierter jOOQ-Artefakte

Das Hello Repository muss die generierten jOOQ-Metadaten verwenden.

## Verboten: Package-by-feature im Backend als primäre Struktur

Backend ist generisch nach technischer Rolle zu paketieren.

---

# 29. README

Erzeuge ein gutes Root `README.md`.

Mindestens:

## Voraussetzungen

So wenige wie möglich.

Erkläre:

* Docker
* Node 24.18.0
* pnpm 11.8.0
* Gradle wird über Wrapper verwendet
* JDK 25 wird über Toolchain bereitgestellt

## Architektur

Kurze Übersicht:

```text
Vue/TS
   ?
Kotlin/JS HelloApi
   ?
HttpHelloRepository
   ?
REST
   ?
Spring Controller
   ?
Backend HelloService
   ?
JooqHelloRepository
   ?
PostgreSQL
```

Shared:

```text
DTO
Repository Interface
Serialization Contract
```

## Bootstrap

Exakte getestete Commands.

## Development

Backend starten.

Frontend starten.

DB starten/stoppen.

Shared npm Package neu bauen.

## Build

Exakte Commands.

## Test

Exakte Commands.

## jOOQ Codegen

Erklären:

* Migration
* DB
* Generator
* generierte Pfade

## ADRs

Link auf:

```text
Design/README.md
```

---

# 30. End-to-End-Verhalten

Nach erfolgreichem Start:

## Backend

```text
http://localhost:8080/api/hello
```

liefert beispielsweise:

```json
{
  "message": "Hello World from database — 2026-07-07T12:34:56.123Z"
}
```

Ein erneuter Request soll wegen des Zeitstempels einen dynamischen Wert liefern.

## Frontend

Die Startseite zeigt:

```text
Hello World from database — 2026-07-07T12:34:56.123Z
```

Die Nachricht muss über folgenden echten Pfad kommen:

```text
PostgreSQL
    ?
generated jOOQ metadata
    ?
JooqHelloRepository
    ?
HelloService
    ?
Spring REST
    ?
Kotlin/JS HttpHelloRepository
    ?
Shared DTO deserialization
    ?
exported Kotlin/JS API
    ?
TypeScript
    ?
Vue page
```

Keinen Teil dieses Flows faken.

---

# 31. Akzeptanzkriterien

Das Projekt gilt erst als fertig, wenn mindestens Folgendes erfüllt ist:

## Gradle

```bash
./gradlew --version
```

zeigt Gradle 9.5.0.

Java Toolchain 25 wird verwendet.

## Datenbank

PostgreSQL startet erfolgreich.

Healthcheck wird grün.

Flyway Migration wird erfolgreich ausgeführt.

`hello_message` enthält mindestens einen Datensatz.

## jOOQ

Code Generation läuft erfolgreich.

Generierte Klassen existieren.

Backend kompiliert dagegen.

Runtime und Codegen verwenden 3.21.5.

## Shared JVM

Shared JVM Target baut.

Backend konsumiert:

```text
project(":shared")
```

## Shared JS

Kotlin/JS baut.

`.d.ts` wird generiert.

npm Package wird gestaged.

## TypeScript

Frontend importiert:

```text
@example/shared
```

über:

```text
workspace:*
```

Kein manuell dupliziertes DTO.

## Async

Shared Repository Interface ist `suspend`.

JS kann die exportierte API mit `await` aufrufen.

Backend behandelt jOOQ/JDBC korrekt als Blocking-I/O.

## Backend

REST Endpoint funktioniert.

## Frontend

Vue-Seite ruft Backend auf.

Dynamische Nachricht wird angezeigt.

## Tests

Alle relevanten Gradle Tests grün.

Frontend Type Check grün.

Frontend Production Build grün.

---

# 32. Tatsächliche Verifikation

Führe die relevanten Befehle wirklich aus.

Mindestens sinngemäß:

```bash
./gradlew clean
./gradlew bootstrap
./gradlew build
pnpm install
pnpm --dir frontend type-check
pnpm --dir frontend build
```

Passe die Reihenfolge an den real implementierten Bootstrap-Prozess an.

Zusätzlich:

1. PostgreSQL starten
2. Backend starten
3. REST Endpoint testen
4. Response prüfen

Beispielsweise mit `curl`.

Falls praktikabel:

5. Frontend starten oder zumindest Preview/Build verifizieren

Stoppe nicht nach einem erfolgreichen Compile, wenn die End-to-End-Kette noch ungeprüft ist.

---

# 33. Abschlussbericht

Nachdem das Projekt erstellt und geprüft wurde, gib einen kompakten Abschlussbericht aus.

Enthalten sein müssen:

1. erzeugte Modulstruktur
2. verwendete Versionen
3. ausgeführte Commands
4. erfolgreiche Tests
5. erfolgreiche Builds
6. REST-Testresultat
7. Pfad der generierten jOOQ-Artefakte
8. Pfad des gestagten npm-Pakets
9. Hinweise auf verbleibende Einschränkungen
10. explizite Nennung, falls irgendein Akzeptanzkriterium nicht erfüllt werden konnte

Keine Erfolge behaupten, die nicht tatsächlich verifiziert wurden.

---

# 34. Prioritäten bei Zielkonflikten

Bei einem technischen Zielkonflikt gilt folgende Reihenfolge:

1. tatsächlich lauffähiger und verifizierter Build
2. festgelegte Architekturentscheidungen
3. keine DTO-Duplizierung
4. gemeinsames suspend Repository Interface
5. korrekte Blocking-I/O-Behandlung auf JVM
6. korrekte Promise-/Async-Nutzung auf JS
7. reproduzierbarer jOOQ-Codegen
8. self-contained Developer Experience
9. minimale Komplexität
10. kosmetische Perfektion

Erstelle jetzt das vollständige Projekt.
