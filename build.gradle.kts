buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.flywaydb:flyway-database-postgresql:12.4.0")
        classpath("org.postgresql:postgresql:42.7.8")
    }
}

plugins {
    base
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.spring.boot) apply false
    alias(libs.plugins.flyway) apply false
    alias(libs.plugins.jooq.codegen) apply false
}

allprojects {
    group = "com.oddjobs"
    version = "0.0.0-local"
}

tasks.register<Exec>("dbUp") {
    group = "oddjobs"
    description = "Start the local PostgreSQL database and wait until the healthcheck is green."
    commandLine("docker", "compose", "up", "--wait", "-d", "postgres")
}

tasks.register<Exec>("dbDown") {
    group = "oddjobs"
    description = "Stop the local PostgreSQL database."
    commandLine("docker", "compose", "down")
}

tasks.register("generateJooq") {
    group = "oddjobs"
    description = "Run Flyway migrations and generate jOOQ sources from the migrated PostgreSQL schema."
    dependsOn(":backend:jooqCodegen")
}

tasks.register("stageSharedNpmPackage") {
    group = "oddjobs"
    description = "Build the Kotlin/JS shared library and stage it as a local npm package."
    dependsOn(":shared:stageNpmPackage")
}

tasks.register("bootstrap") {
    group = "oddjobs"
    description = "Start DB, migrate schema, generate jOOQ, build shared JVM/JS artifacts, and stage the shared npm package."
    dependsOn(":backend:jooqCodegen")
    dependsOn(":shared:build")
    dependsOn(":shared:stageNpmPackage")
}

gradle.projectsEvaluated {
    project(":backend").tasks.named("flywayMigrate") {
        dependsOn(rootProject.tasks.named("dbUp"))
    }
}
