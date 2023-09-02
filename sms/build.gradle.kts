import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot")
    id("io.spring.dependency-management")
    kotlin("jvm")
    kotlin("plugin.spring")
}

group = "com.fone"
version = "1.0.0"

java {
    sourceCompatibility = JavaVersion.VERSION_17
}

dependencies {
    compileOnly("org.springframework.boot:spring-boot-starter-webflux")
    compileOnly("com.fasterxml.jackson.module:jackson-module-kotlin")
    compileOnly("io.projectreactor.kotlin:reactor-kotlin-extensions")
    compileOnly("org.jetbrains.kotlin:kotlin-reflect")
    compileOnly("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    compileOnly("io.springfox:springfox-boot-starter:3.0.0")
    testImplementation("org.springframework.boot:spring-boot-starter-webflux")
    testImplementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    testImplementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    testImplementation("org.jetbrains.kotlin:kotlin-reflect")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("io.kotest:kotest-runner-junit5:5.5.4")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.2")
    testImplementation("io.mockk:mockk:1.12.0")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "17"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}
