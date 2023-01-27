dependencies {
    // swagger
    implementation("io.springfox:springfox-boot-starter:3.0.0")

    // lombok
    implementation("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // mysql db reactive
    implementation("io.agroal:agroal-pool:2.0")
    implementation("mysql:mysql-connector-java")
    implementation("io.vertx:vertx-mysql-client:4.3.7")

    // kotlin-jdsl
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    val jdslVersion = "2.2.0.RELEASE"
    implementation("com.linecorp.kotlin-jdsl:spring-data-kotlin-jdsl-starter:$jdslVersion")

    // reactive
    implementation("com.linecorp.kotlin-jdsl:spring-data-kotlin-jdsl-hibernate-reactive:$jdslVersion")
    implementation("org.hibernate.reactive:hibernate-reactive-core:1.1.9.Final")
    implementation("io.smallrye.reactive:mutiny-kotlin:1.6.0")

    // Kotlin 로깅
    implementation("io.github.microutils:kotlin-logging:2.1.21")
}