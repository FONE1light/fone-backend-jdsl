plugins {
    kotlin("plugin.jpa") version "1.7.0"
}

dependencies {
    implementation(project(path = ":common", configuration = "default"))
    implementation(project(":home"))
    implementation(project(":user"))
    implementation(project(":jobOpening"))
    implementation(project(":question"))
    implementation(project(":profile"))
    implementation(project(":competition"))
    implementation(project(":report"))
    implementation(project(":idl"))

    // armeria
    implementation("com.linecorp.armeria:armeria-grpc")
    implementation("com.linecorp.armeria:armeria-spring-boot2-webflux-starter")

    // test
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(module = "mockito-core")
    }
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("com.ninja-squad:springmockk:3.0.1")
    testImplementation("junit","junit","4.13.2")
    testImplementation("io.kotest:kotest-runner-junit5:5.5.4")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.2")
    testImplementation("io.mockk:mockk:1.12.0")

    // testcontainers
    testImplementation("org.testcontainers:testcontainers:1.17.1")
    testImplementation("org.testcontainers:junit-jupiter:1.17.1")
    testImplementation("org.testcontainers:mysql:1.17.1")
}

tasks.jar { enabled = false }
tasks.bootJar { enabled = true }

dependencyManagement {
    imports {
        mavenBom("com.linecorp.armeria:armeria-bom:0.99.9")
        mavenBom("io.netty:netty-bom:4.1.51.Final")
    }
}