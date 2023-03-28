dependencies {

    // Webflux
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    // Kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // Kotlin 로깅
    implementation("io.github.microutils:kotlin-logging:2.1.21")
}

tasks.jar { enabled = false }

tasks.bootJar { enabled = true }
