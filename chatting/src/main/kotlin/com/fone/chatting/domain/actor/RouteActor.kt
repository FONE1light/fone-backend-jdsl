package com.fone.chatting.domain.actor

import com.fone.chatting.presentation.jsonMapper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.actor
import kotlinx.coroutines.reactor.awaitSingleOrNull
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.toMono

fun routeActor(session: WebSocketSession) =
    CoroutineScope(Dispatchers.Default).actor<UserOutgoingMessage> {
        val jsonMapper = jsonMapper()

        for (msg in channel) {
            session.send(
                session.textMessage(jsonMapper.writeValueAsString(msg)).toMono()
            ).awaitSingleOrNull()
        }
    }
