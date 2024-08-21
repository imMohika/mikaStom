plugins {
    id("java")
    id("com.gradleup.shadow") version "8.3.0"
    id("io.freefair.lombok") version "8.7.1"
}

group = "ir.mohika"
version = "0.0-SNAPSHOT"

repositories {
    mavenCentral()
    maven("https://repo.panda-lang.org/releases")
    maven("https://repo.emortal.dev/snapshots")
    maven("https://repo.emortal.dev/releases")
}

dependencies {
    implementation("net.minestom:minestom-snapshots:6c5cd6544e")

    implementation("org.slf4j:slf4j-api:2.0.16")
    implementation("ch.qos.logback:logback-classic:1.5.6")

    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    implementation("com.fasterxml.jackson.core:jackson-databind:2.15.1")

    implementation("de.exlll:configlib-yaml:4.5.0")

    implementation("net.kyori:adventure-text-minimessage:4.17.0")

    implementation("dev.rollczi:litecommands-minestom:3.4.3")

    implementation("dev.hollowcube:schem:1.2.0")
    implementation("dev.hollowcube:polar:1.11.1")

    implementation("dev.emortal.api:module-system:1.0.0")

    implementation("com.github.ben-manes.caffeine:caffeine:3.1.8")

    implementation("com.h2database:h2:2.3.232")

    implementation("org.xerial:sqlite-jdbc:3.46.1.0")
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

tasks.withType<JavaCompile> {
    options.compilerArgs.add("-parameters")
}
