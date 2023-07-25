package com.fone.chatting.domain.actor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import mu.KotlinLogging
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

sealed class RoomActorMsg

data class Join(val username: String, val channel: SendChannel<UserActorMsg>) : RoomActorMsg()

data class Terminated(val username: String) : RoomActorMsg()

data class IncomingMessage(val username: String, val message: String) : RoomActorMsg()

data class MessageRead(val messageId: String) : RoomActorMsg()

private val log = KotlinLogging.logger("roomActorLogger")

@ObsoleteCoroutinesApi
fun roomActor(roomId: Int) = CoroutineScope(Dispatchers.Default).actor<RoomActorMsg> {
    val users = ConcurrentHashMap<String, SendChannel<UserActorMsg>>()
    val messages = mutableListOf<UserIncomingMessage>()

    suspend fun broadCast(outgoingMessage: UserOutgoingMessage) = users.values.forEach { it.send(outgoingMessage) }

    for (msg in channel) {
        when (msg) {
            is Join -> {
                users[msg.username] = msg.channel
                log.info {
                    "${msg.username} joined room $roomId, current user list: ${users.keys}"
                }
            }

            is IncomingMessage -> {
                val outgoingMessage = UserOutgoingMessage(
                    "user_incoming_message",
                    UUID.randomUUID().toString(),
                    msg.username,
                    msg.message,
                    true
                )
                broadCast(outgoingMessage)
                log.info { "${msg.username} sent message: ${msg.message}" }
            }

            is Terminated -> {
                users.remove(msg.username)
                log.info { "${msg.username} left room $roomId, current user list: ${users.keys}" }
            }

            is MessageRead -> {
                // 메시지 읽음 상태를 처리합니다.
                val messageId = msg.messageId
                val readMessage = messages.find { it.messageId == messageId }
                if (readMessage != null) {
                    // 클라이언트에게 메시지 읽음 상태를 보냅니다.
                    val outgoingMessage = UserOutgoingMessage(
                        "message_read",
                        UUID.randomUUID().toString(),
                        readMessage.username,
                        readMessage.message,
                        true
                    )
                    broadCast(outgoingMessage)
                }
            }
        }
    }
}
