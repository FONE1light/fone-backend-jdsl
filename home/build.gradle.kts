dependencies { implementation(project(path = ":common", configuration = "default")) }

tasks.bootJar { enabled = false }
