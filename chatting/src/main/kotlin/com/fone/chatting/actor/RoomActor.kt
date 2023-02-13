package com.fone.chatting.actor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor
import org.slf4j.LoggerFactory
import java.util.concurrent.ConcurrentHashMap

sealed class RoomActorMsg

data class Join(val username: String, val channel: SendChannel<UserActorMsg>) : RoomActorMsg()
data class Terminated(val username: String) : RoomActorMsg()
data class IncomingMessage(val username: String, val message: String) : RoomActorMsg()

@ObsoleteCoroutinesApi
fun roomActor(roomId: Int) = CoroutineScope(Dispatchers.Default).actor<RoomActorMsg> {
    val log = LoggerFactory.getLogger("roomActorLogger")

    val users = ConcurrentHashMap<String, SendChannel<UserActorMsg>>()

    suspend fun broadCast(outgoingMessage: UserOutgoingMessage) = users.values.forEach {
        it.send(outgoingMessage)
    }

    for (msg in channel) {
        when (msg) {
            is Join -> {
                users[msg.username] = msg.channel
                broadCast(UserOutgoingMessage("admin", "${msg.username} joined."))
                log.info("${msg.username} joined room $roomId, current user list: ${users.keys}")
            }

            is IncomingMessage -> {
                broadCast(UserOutgoingMessage(msg.username, msg.message))
                log.info("${msg.username} sent message: ${msg.message}")
            }

            is Terminated -> {
                users.remove(msg.username)
                broadCast(UserOutgoingMessage("admin", "${msg.username} left."))
                log.info("${msg.username} left room $roomId, current user list: ${users.keys}")
            }
        }
    }
}
