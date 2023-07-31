dependencies {
    // Persistence
    implementation("javax.persistence:javax.persistence-api")

    // Swagger
    implementation("io.springfox:springfox-boot-starter:3.0.0")

    // Mysql DB Reactive
    implementation("mysql:mysql-connector-java")
    implementation("io.vertx:vertx-mysql-client:4.3.7")

    // Kotlin-jdsl
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    val jdslVersion = "2.2.0.RELEASE"
    implementation("com.linecorp.kotlin-jdsl:spring-data-kotlin-jdsl-starter:$jdslVersion")

    // Reactive
    implementation(
        "com.linecorp.kotlin-jdsl:spring-data-kotlin-jdsl-hibernate-reactive:$jdslVersion"
    )
    implementation("org.hibernate.reactive:hibernate-reactive-core:1.1.9.Final")
    implementation("io.smallrye.reactive:mutiny-kotlin:1.6.0")

    // Kotlin 로깅
    implementation("io.github.microutils:kotlin-logging:2.1.21")

    // Security
    implementation("org.springframework.boot:spring-boot-starter-security")

    // Redis
    implementation("org.springframework.boot:spring-boot-starter-data-redis")

    // JWT 인증
    implementation("io.jsonwebtoken:jjwt-api:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:0.11.5")
    runtimeOnly("io.jsonwebtoken:jjwt-jackson:0.11.5")

    // Validation
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.hibernate.validator:hibernate-validator:6.1.2.Final")

    // Webflux
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")

    // AWS SDK
    implementation("software.amazon.awssdk:ses:2.20.114")

    // Kotlin
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")

    val coroutineVersion = "1.6.3"
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:$coroutineVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactive:$coroutineVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutineVersion")
    runtimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:$coroutineVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:$coroutineVersion")

    // Annotation Processor
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.bootJar { enabled = false }
