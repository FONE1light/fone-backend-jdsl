package com.fone.chatting.common.config

import com.fone.chatting.presentation.WSHandler
import kotlinx.coroutines.InternalCoroutinesApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.reactive.HandlerMapping
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter

@Configuration
class WebsocketConfig {
    @Bean
    @InternalCoroutinesApi
    fun handlerMapping(): HandlerMapping {
        val map = mapOf("/chat" to WSHandler())
        val order = -1 // before annotated controllers

        return SimpleUrlHandlerMapping(map, order)
    }

    @Bean
    fun handlerAdapter() = WebSocketHandlerAdapter()
}