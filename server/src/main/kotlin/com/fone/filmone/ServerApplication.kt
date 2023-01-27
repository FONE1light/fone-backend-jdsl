package com.fone.filmone

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication(scanBasePackages = ["com.fone"])
class ServerApplication

fun main(args: Array<String>) {
    runApplication<ServerApplication>(*args)
}