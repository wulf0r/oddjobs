import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.spring.boot)
    alias(libs.plugins.flyway)
    alias(libs.plugins.jooq.codegen)
    alias(libs.plugins.kotlin.spring)
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(25))
    }
}

kotlin {
    jvmToolchain(25)
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_25)
        freeCompilerArgs.add("-Xjsr305=strict")
    }
}

val dbUrl = providers.gradleProperty("oddjobs.db.url")
    .orElse(providers.environmentVariable("ODDJOBS_DB_URL"))
    .orElse("jdbc:postgresql://localhost:9993/helloworld")
val dbUser = providers.gradleProperty("oddjobs.db.user")
    .orElse(providers.environmentVariable("ODDJOBS_DB_USER"))
    .orElse("helloworld")
val dbPassword = providers.gradleProperty("oddjobs.db.password")
    .orElse(providers.environmentVariable("ODDJOBS_DB_PASSWORD"))
    .orElse("helloworld")
val generatedJooqDir = layout.buildDirectory.dir("generated-src/jooq/main")

sourceSets {
    main {
        kotlin.srcDir(generatedJooqDir)
    }
}

dependencies {
    implementation(platform("org.springframework.boot:spring-boot-dependencies:${libs.versions.spring.boot.get()}"))
    developmentOnly(platform("org.springframework.boot:spring-boot-dependencies:${libs.versions.spring.boot.get()}"))
    testImplementation(platform("org.springframework.boot:spring-boot-dependencies:${libs.versions.spring.boot.get()}"))

    implementation(project(":shared"))

    implementation("org.springframework.boot:spring-boot-starter-webmvc")
    implementation("org.springframework.boot:spring-boot-starter-jooq")
    implementation("org.springframework.boot:spring-boot-starter-flyway")
    implementation("org.springframework.boot:spring-boot-starter-kotlinx-serialization-json")
    implementation(libs.kotlin.reflect)
    implementation(libs.coroutines.reactor)
    implementation(libs.jooq)
    implementation(libs.flyway.postgresql)
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    runtimeOnly(libs.postgresql) {
        version { strictly(libs.versions.postgresql.jdbc.get()) }
    }

    jooqCodegen(libs.postgresql) {
        version { strictly(libs.versions.postgresql.jdbc.get()) }
    }

    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webmvc-test")
    testImplementation(kotlin("test"))
    testImplementation(libs.coroutines.test)
    testImplementation(libs.testcontainers.junit)
    testImplementation(libs.testcontainers.postgresql)
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

flyway {
    url = dbUrl.get()
    user = dbUser.get()
    password = dbPassword.get()
    locations = arrayOf("filesystem:${projectDir}/src/main/resources/db/migration")
}

jooq {
    configuration {
        jdbc {
            driver = "org.postgresql.Driver"
            url = dbUrl.get()
            user = dbUser.get()
            password = dbPassword.get()
        }
        generator {
            name = "org.jooq.codegen.KotlinGenerator"
            database {
                name = "org.jooq.meta.postgres.PostgresDatabase"
                inputSchema = "public"
                //includes = "hello_message"
            }
            generate {
                isDaos = false
                isPojos = false
                isRecords = true
                isDeprecated = false
                isKotlinNotNullRecordAttributes = true
                isKotlinNotNullPojoAttributes = true
                isKotlinNotNullInterfaceAttributes = true
            }
            target {
                packageName = "com.oddjobs.backend.generated.jooq"
                directory = generatedJooqDir.get().asFile.absolutePath
            }
        }
    }
}

tasks.named("jooqCodegen") {
    dependsOn("flywayMigrate")
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}

tasks.named("compileKotlin") {
    dependsOn("jooqCodegen")
}
