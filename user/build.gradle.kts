plugins {
    kotlin("plugin.jpa") version "1.7.0"
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    implementation(project(path = ":common", configuration = "default"))
    // build.gradle.kts
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude(module = "mockito-core")
    }
    testImplementation("org.junit.jupiter:junit-jupiter-api")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("com.ninja-squad:springmockk:3.0.1")
}

tasks.bootJar { enabled = false }