plugins {
	kotlin("jvm") version "1.7.21"
	id("com.google.protobuf") version "0.8.15"
}

allprojects {
	group = "com.fone.filmone"
	version = "0.0.1-SNAPSHOT"

	apply(plugin = "kotlin")

	repositories {
		mavenCentral()
	}

	dependencies {
		implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1")
	}
}
