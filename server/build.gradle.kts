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
}

tasks.jar { enabled = false }
tasks.bootJar { enabled = true }

dependencyManagement {
    imports {
        mavenBom("com.linecorp.armeria:armeria-bom:0.99.9")
        mavenBom("io.netty:netty-bom:4.1.51.Final")
    }
}