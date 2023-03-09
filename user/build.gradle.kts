plugins {
    kotlin("plugin.jpa") version "1.7.0"

    id("io.kotest") version "0.3.8"
}

dependencies {
    implementation(project(path = ":common", configuration = "default"))

    // Test
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("junit", "junit", "4.13.2")
    testImplementation("io.kotest:kotest-runner-junit5:5.5.4")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.2")
    testImplementation("io.mockk:mockk:1.12.0")
}

tasks.bootJar { enabled = false }
