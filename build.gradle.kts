import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import org.jlleitschuh.gradle.ktlint.KtlintExtension
import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("org.springframework.boot") version "2.7.5"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
    id("org.jlleitschuh.gradle.ktlint") version "11.3.1"
    id("io.gitlab.arturbosch.detekt") version "1.21.0"
    id("org.jetbrains.dokka") version "1.7.20"

    id("com.google.protobuf") version "0.8.15"

    kotlin("jvm") version "1.7.0"
    kotlin("plugin.spring") version "1.7.0"
    kotlin("kapt") version "1.6.21"
    kotlin("plugin.jpa") version "1.7.0"
    kotlin("plugin.allopen") version "1.7.0"
    kotlin("plugin.noarg") version "1.7.0"
}

group = "com.fone.filmone"
version = "0.0.1-SNAPSHOT"

detekt {
    toolVersion = "1.20.0"
    config = files("config/detekt/detekt.yml")
    buildUponDefaultConfig = true
}

allprojects {
    repositories { mavenCentral() }

    tasks {
        withType<Assemble> {
            dependsOn("ktlintFormat")
        }

        withType<KotlinCompile> {
            kotlinOptions {
                freeCompilerArgs = listOf("-Xjsr305=strict")
                jvmTarget = "17"
            }
        }

        withType<Test> {
            useJUnitPlatform()
            systemProperty("file.encoding", "UTF-8")
        }

        withType<BootJar> {
            enabled = false
        }
    }
}

subprojects {
    apply {
        plugin("idea")
        plugin("kotlin")
        plugin("kotlin-kapt")
        plugin("kotlin-spring")
        plugin("org.jlleitschuh.gradle.ktlint")
        plugin("org.springframework.boot")
        plugin("io.spring.dependency-management")
        plugin("org.jetbrains.dokka")
        plugin("io.gitlab.arturbosch.detekt")
    }

    java.sourceCompatibility = JavaVersion.VERSION_17
    java.targetCompatibility = JavaVersion.VERSION_17

    group = "com.fone.filmone.${path.split(":")[1]}"
    version = "0.0.1-SNAPSHOT"

    tasks {
        withType<Jar> {
            archiveFileName.set(
                project.path.split(":").drop(1).joinToString(separator = "-", postfix = "-") + project.version + ".jar"
            )
        }

        withType<BootJar> {
            enabled = false
        }

        withType<Test> {
            useJUnitPlatform()
            systemProperty("file.encoding", "UTF-8")
        }
    }

    dependencies {}

    dependencyManagement {
        imports {
            mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
        }
    }

    configure<KtlintExtension> {
        filter {
            exclude { element -> element.file.path.contains("generated/") }
        }
    }
}

tasks.dokkaHtmlMultiModule.configure {
    outputDirectory.set(buildDir.resolve("$rootDir/devdocs"))
}
