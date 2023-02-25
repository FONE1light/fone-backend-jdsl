plugins {
    kotlin("plugin.jpa") version "1.7.0"
}

tasks.test {
    useJUnitPlatform()
}

dependencies {
    implementation(project(path = ":common", configuration = "default"))
}

tasks.bootJar { enabled = false }