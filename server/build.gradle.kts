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

    // persistence
    implementation("javax.persistence:javax.persistence-api")

    // security
    implementation("org.springframework.boot:spring-boot-starter-security")

    // kotlin-jdsl
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    val jdslVersion = "2.2.0.RELEASE"
    implementation("com.linecorp.kotlin-jdsl:spring-data-kotlin-jdsl-starter:$jdslVersion")

    // mysql db reactive
    implementation("io.agroal:agroal-pool:2.0")
    implementation("mysql:mysql-connector-java")
    implementation("io.vertx:vertx-mysql-client:4.3.7")

    val coroutineVersion = "1.6.3"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:$coroutineVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive:$coroutineVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion")
    runtimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:$coroutineVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:$coroutineVersion")

    // reactive
    implementation("com.linecorp.kotlin-jdsl:spring-data-kotlin-jdsl-hibernate-reactive:$jdslVersion")
    implementation("org.hibernate.reactive:hibernate-reactive-core:1.1.9.Final")
    implementation("io.smallrye.reactive:mutiny-kotlin:1.6.0")

    // lombok
    implementation("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // armeria
    implementation("com.linecorp.armeria:armeria-grpc")
    implementation("com.linecorp.armeria:armeria-spring-boot2-webflux-starter")

    implementation(project(":idl"))

    // Kotlin 로깅
    implementation("io.github.microutils:kotlin-logging:2.1.21")

    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    // Swagger
    implementation("io.springfox:springfox-boot-starter:3.0.0")

    // Annotation Processor
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.jar { enabled = false }
tasks.bootJar { enabled = true }

dependencyManagement {
    imports {
        mavenBom("com.linecorp.armeria:armeria-bom:0.99.9")
        mavenBom("io.netty:netty-bom:4.1.51.Final")
    }
}