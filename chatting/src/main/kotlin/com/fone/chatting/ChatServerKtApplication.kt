package com.fone.chatting

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication class ChatServerKtApplication

fun main(args: Array<String>) {
    runApplication<ChatServerKtApplication>(*args)
}
