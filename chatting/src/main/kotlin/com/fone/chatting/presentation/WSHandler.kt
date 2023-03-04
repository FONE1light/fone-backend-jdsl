package com.fone.chatting.presentation

import com.fone.chatting.domain.Room
import com.fone.chatting.domain.actor.Completed
import com.fone.chatting.domain.actor.Connected
import com.fone.chatting.domain.actor.UserIncomingMessage
import com.fone.chatting.domain.actor.routeActor
import com.fone.chatting.domain.actor.userActor
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.reactive.asFlow
import kotlinx.coroutines.reactor.mono
import org.springframework.web.reactive.socket.WebSocketHandler
import org.springframework.web.reactive.socket.WebSocketSession
import reactor.core.publisher.Mono

@InternalCoroutinesApi
class WSHandler : WebSocketHandler {
    override fun handle(session: WebSocketSession): Mono<Void> =
        mono { handleSuspended(session) }.then()

    private suspend fun handleSuspended(session: WebSocketSession) {
        val params = parseQueryString(session.handshakeInfo.uri)
        val roomId = params["id"]!!.toInt()
        val username = params["name"] ?: "anonymous"

        val roomActor = Room.findOrCreate(roomId)
        val userActor = userActor(roomActor)

        val routeActor = routeActor(session)

        val connectedMsg = Connected(routeActor = routeActor, username = username)

        userActor.send(connectedMsg)

        session
            .receive()
            .log()
            .map { it.retain() }
            .asFlow()
            .onCompletion { userActor.send(Completed) }
            .collect {
                val userIncomingMessage = UserIncomingMessage(username, it.payloadAsText, "0")

                userActor.send(userIncomingMessage)
            }
    }
}
