import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsEnvSpec
import org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsPlugin

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
}

plugins.withType<NodeJsPlugin> {
    the<NodeJsEnvSpec>().version = "24.18.0"
}

kotlin {
    jvmToolchain(25)

    jvm {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_25)
        }
    }

    js {
        outputModuleName = "shared"
        useEsModules()
        browser {
            binaries.library()
        }
        generateTypeScriptDefinitions()
        compilerOptions {
            freeCompilerArgs.add("-Xenable-suspend-function-exporting")
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.coroutines.core)
            implementation(libs.serialization.json)
        }
        commonTest.dependencies {
            implementation(kotlin("test"))
        }
        jsMain.dependencies {
            implementation(libs.coroutines.core)
        }
    }
}

val stagedPackageDir = rootProject.layout.projectDirectory.dir("packages/shared")
val stagedDistDir = stagedPackageDir.dir("dist")

val writeStagedPackageJson by tasks.registering {
    group = "build"
    description = "Write package metadata for the staged Kotlin/JS npm package."
    outputs.file(stagedPackageDir.file("package.json"))
    doLast {
        val file = stagedPackageDir.file("package.json").asFile
        file.parentFile.mkdirs()
        file.writeText(
            """{
  "name": "@example/shared",
  "version": "0.0.0-local",
  "private": true,
  "type": "module",
  "exports": {
    ".": {
      "types": "./dist/shared.d.mts",
      "import": "./dist/shared.mjs"
    }
  },
  "types": "./dist/shared.d.mts",
  "main": "./dist/shared.mjs",
  "module": "./dist/shared.mjs"
}
""",
        )
    }
}

tasks.register<Sync>("stageNpmPackage") {
    group = "build"
    description = "Stage the Kotlin/JS production library distribution and TypeScript definitions for pnpm workspace consumption."
    dependsOn("jsBrowserProductionLibraryDistribution", writeStagedPackageJson)
    into(stagedDistDir)
    duplicatesStrategy = DuplicatesStrategy.INCLUDE

    from(layout.buildDirectory.dir("dist/js/productionLibrary"))
    from(layout.buildDirectory.dir("js/packages/shared/kotlin")) {
        include("*.d.ts", "*.js", "*.mjs", "*.map")
    }
}
