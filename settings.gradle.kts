rootProject.name = "fone-backend"

include(":server")
include(":home")
include(":common")
include(":chatting")

pluginManagement {
    plugins {
        id("org.jlleitschuh.gradle.ktlint") version "11.3.1"
    }
}
