rootProject.name = "fone-backend"

include(":idl")
include(":server")
include(":home")
include(":common")
include(":chatting")
include(":jobOpening")
include(":question")
include(":profile")
include(":competition")
include(":report")

pluginManagement {
    plugins {
        id("org.jlleitschuh.gradle.ktlint") version "11.3.1"
    }
}
