package com.fone.chatting.domain.actor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import mu.KotlinLogging
import java.util.concurrent.ConcurrentHashMap

sealed class RoomActorMsg

data class Join(val username: String, val channel: SendChannel<UserActorMsg>) : RoomActorMsg()

data class Terminated(val username: String) : RoomActorMsg()

data class IncomingMessage(val username: String, val message: String) : RoomActorMsg()

private val log = KotlinLogging.logger("roomActorLogger")

@ObsoleteCoroutinesApi
fun roomActor(roomId: Int) =
    CoroutineScope(Dispatchers.Default).actor<RoomActorMsg> {
        val users = ConcurrentHashMap<String, SendChannel<UserActorMsg>>()

        suspend fun broadCast(outgoingMessage: UserOutgoingMessage) =
            users.values.forEach { it.send(outgoingMessage) }

        val userOutgoingMessages = mutableListOf<UserOutgoingMessage>()

        suspend fun broadCastAll(
            username: String,
            users: ConcurrentHashMap<String, SendChannel<UserActorMsg>>,
            msg: Join,
        ) {
            if (users[username] != null) {
                return
            }

            val outgoingMessage = UserOutgoingMessage(msg.username, "", "")
            broadCast(outgoingMessage)

            users[msg.username] = msg.channel

            userOutgoingMessages.forEach {
                if (username == it.author) {
                    it.count = it.count
                } else {
                    it.count = "0"
                }
                broadCast(it)
            }
        }

        for (msg in channel) {
            when (msg) {
                is Join -> {
                    broadCastAll(msg.username, users, msg)
                    log.info {
                        "${msg.username} joined room $roomId, current user list: ${users.keys}"
                    }
                }

                is IncomingMessage -> {
                    val outgoingMessage =
                        UserOutgoingMessage(
                            msg.username,
                            msg.message,
                            (1 - users.values.count() + 1).toString()
                        )
                    userOutgoingMessages.add(outgoingMessage)
                    broadCast(outgoingMessage)
                    log.info { "${msg.username} sent message: ${msg.message}" }
                }

                is Terminated -> {
                    users.remove(msg.username)
                    log.info { "${msg.username} left room $roomId, current user list: ${users.keys}" }
                }
            }
        }
    }
