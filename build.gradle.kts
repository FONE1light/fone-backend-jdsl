import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "2.7.5"
	id("io.spring.dependency-management") version "1.0.15.RELEASE"
	kotlin("jvm") version "1.7.0"
	kotlin("plugin.spring") version "1.7.0"
	kotlin("kapt") version "1.6.21"

	id("com.google.protobuf") version "0.8.15"
	id("io.kotest") version "0.3.8"
}

allprojects {
	repositories {
		mavenCentral()
	}

}

subprojects {
	group = "com.fone.filmone"
	version = "0.0.1-SNAPSHOT"

	apply(plugin = "kotlin")
	apply(plugin = "kotlin-spring")
	apply(plugin = "io.spring.dependency-management")
	apply(plugin = "kotlin-kapt")
	apply(plugin = "org.springframework.boot")

	java.sourceCompatibility = JavaVersion.VERSION_17
	java.targetCompatibility = JavaVersion.VERSION_17

	dependencies {

	}

	dependencyManagement {
		imports {
			mavenBom(org.springframework.boot.gradle.plugin.SpringBootPlugin.BOM_COORDINATES)
		}
	}

	tasks.withType<KotlinCompile> {
		kotlinOptions {
			freeCompilerArgs = listOf("-Xjsr305=strict")
			jvmTarget = "17"
		}
	}

	tasks.withType<Test> {
		enabled = false
		useJUnitPlatform()
	}
}