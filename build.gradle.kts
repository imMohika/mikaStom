plugins {
    id("java")
    id("com.gradleup.shadow") version "8.3.0"
    id("io.freefair.lombok") version "8.7.1"
}

group = "ir.mohika"
version = "0.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.minestom:minestom-snapshots:6c5cd6544e")

    implementation("org.slf4j:slf4j-api:2.0.16")
    implementation("ch.qos.logback:logback-classic:1.5.6")

    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.1")

    implementation("de.exlll:configlib-yaml:4.5.0")
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

tasks {
    jar {
        manifest {
            attributes["Main-Class"] = "ir.mohika.mikastom.MikaStom"
        }
    }

    build {
        dependsOn(shadowJar)
    }

    shadowJar {
        mergeServiceFiles()
        archiveBaseName.set("mikastom")
        // Prevent the -all suffix on the shadowjar file.
        archiveClassifier.set("")
    }
}
