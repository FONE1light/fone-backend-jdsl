package com.fone.chatting.domain.actor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor

sealed class UserActorMsg

class Connected(val routeActor: SendChannel<UserOutgoingMessage>, val username: String) :
    UserActorMsg()

object Completed : UserActorMsg()

data class UserIncomingMessage(
    val type: String,
    val messageId: String,
    val username: String,
    val message: String,
    val isRead: Boolean,
) :
    UserActorMsg()

data class UserOutgoingMessage(
    val type: String,
    val messageId: String,
    val author: String,
    val message: String,
    var isRead: Boolean,
) :
    UserActorMsg()

fun userActor(roomActor: SendChannel<RoomActorMsg>) =
    CoroutineScope(Dispatchers.Default).actor<UserActorMsg> {
        lateinit var routeActor: SendChannel<UserOutgoingMessage>
        lateinit var username: String
        val roomActor = roomActor

        for (msg in channel) {
            when (msg) {
                is Connected -> {
                    roomActor.send(Join(msg.username, this.channel))
                    routeActor = msg.routeActor
                    username = msg.username
                }

                is Completed -> {
                    roomActor.send(Terminated(username))
                }

                is UserIncomingMessage -> {
                    roomActor.send(IncomingMessage(msg.username, msg.message))
                }

                is UserOutgoingMessage -> {
                    routeActor.send(msg)
                }
            }
        }
    }
