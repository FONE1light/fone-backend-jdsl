rootProject.name = "backend"

include(":idl")
include(":server")
project(":server").projectDir = file("./server")