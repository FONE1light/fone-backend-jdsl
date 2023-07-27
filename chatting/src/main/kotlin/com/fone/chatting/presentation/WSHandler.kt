package com.fone.chatting.presentation

import com.fasterxml.jackson.databind.JsonNode
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fone.chatting.domain.Room
import com.fone.chatting.domain.actor.Completed
import com.fone.chatting.domain.actor.Connected
import com.fone.chatting.domain.actor.MessageRead
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
import java.util.UUID

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
                val payload = it.payloadAsText
                val data = parsePayload(payload)

                when (data.type) {
                    "user_incoming_message" -> {
                        val messageId = generateMessageId()
                        val userIncomingMessage =
                            UserIncomingMessage(
                                "user_incoming_message",
                                messageId,
                                username,
                                data.message ?: "",
                                false
                            )
                        userActor.send(userIncomingMessage)
                    }

                    "message_read" -> {
                        val author = data.author.toString()
                        val messageRead = MessageRead(author)
                        roomActor.send(messageRead)
                    }
                }
            }
    }

    private fun generateMessageId(): String {
        return UUID.randomUUID().toString()
    }

    data class Message(
        val type: String,
        val messageId: String? = null,
        val author: String? = null,
        val message: String? = null,
        val isRead: Boolean = false,
    )

    private fun parsePayload(payload: String): Message {
        val mapper: ObjectMapper = jacksonObjectMapper()
        val jsonNode: JsonNode = mapper.readTree(payload)

        val type = jsonNode["type"]?.textValue() ?: ""
        val messageId = jsonNode["messageId"]?.textValue()
        val author = jsonNode["author"]?.textValue()
        val message = jsonNode["message"]?.textValue()
        val isRead = jsonNode["isRead"]?.booleanValue() ?: false

        return Message(type, messageId, author, message, isRead)
    }
}
