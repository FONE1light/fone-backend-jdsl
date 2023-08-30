package com.fone.sms

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.web.reactive.config.EnableWebFlux

@SpringBootApplication
@EnableWebFlux
class SmsApplication

fun main(args: Array<String>) {
    runApplication<SmsApplication>(*args)
}
