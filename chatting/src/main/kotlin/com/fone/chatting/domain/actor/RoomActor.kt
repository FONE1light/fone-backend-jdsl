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

data class MessageRead(val username: String) : RoomActorMsg()

private val log = KotlinLogging.logger("roomActorLogger")

@ObsoleteCoroutinesApi
fun roomActor(roomId: Int) = CoroutineScope(Dispatchers.Default).actor<RoomActorMsg> {
    val users = ConcurrentHashMap<String, SendChannel<UserActorMsg>>()
    val userOutgoingMessages = mutableListOf<UserOutgoingMessage>()

    suspend fun broadCast(outgoingMessage: UserOutgoingMessage) = users.values.forEach { it.send(outgoingMessage) }

    suspend fun sendPrevMessagesToNewUser(
        userOutgoingMessages: MutableList<UserOutgoingMessage>,
        msg: Join,
    ) {
        userOutgoingMessages.forEach {
            msg.channel.send(it)
        }
    }

    fun markOutgoingMessagesAsRead(
        userOutgoingMessages: MutableList<UserOutgoingMessage>,
        username: String,
    ) {
        userOutgoingMessages.map {
            if (it.author != username) {
                it.isRead = true
            }
            it
        }
    }

    for (msg in channel) {
        when (msg) {
            is Join -> {
                users[msg.username] = msg.channel
                log.info {
                    "${msg.username} joined room $roomId, current user list: ${users.keys}"
                }

                broadCast(UserOutgoingMessage("message_read", "", msg.username, "", true))

                markOutgoingMessagesAsRead(userOutgoingMessages, msg.username)

                sendPrevMessagesToNewUser(userOutgoingMessages, msg)
            }

            is IncomingMessage -> {
                val outgoingMessage = UserOutgoingMessage(
                    "user_incoming_message",
                    UUID.randomUUID().toString(),
                    msg.username,
                    msg.message,
                    false
                )
                broadCast(outgoingMessage)
                log.info { "${msg.username} sent message: ${msg.message}" }
                userOutgoingMessages.add(outgoingMessage)
            }

            is Terminated -> {
                users.remove(msg.username)
                log.info { "${msg.username} left room $roomId, current user list: ${users.keys}" }
            }

            is MessageRead -> {
                broadCast(UserOutgoingMessage("message_read", "", msg.username, "", true))

                markOutgoingMessagesAsRead(userOutgoingMessages, msg.username)
            }
        }
    }
}
