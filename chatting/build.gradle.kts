dependencies {

    // Webflux
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    // Kotlin
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
}

tasks.jar { enabled = false }
tasks.bootJar { enabled = true }