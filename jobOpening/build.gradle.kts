plugins { kotlin("plugin.jpa") version "1.7.0" }

dependencies { implementation(project(path = ":common", configuration = "default")) }

tasks.bootJar { enabled = false }
