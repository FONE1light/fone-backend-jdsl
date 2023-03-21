dependencies {
    implementation(project(path = ":common", configuration = "default"))

    // logging
    implementation("io.github.microutils:kotlin-logging-jvm:3.0.5")

    // tests
    testImplementation("io.kotest:kotest-runner-junit5:5.5.4")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.2")
    testImplementation("io.mockk:mockk:1.12.0")
}

tasks.bootJar { enabled = false }
