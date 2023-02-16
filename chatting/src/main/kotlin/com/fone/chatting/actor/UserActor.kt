package com.fone.chatting.actor

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.channels.actor

sealed class UserActorMsg

class Connected(val routeActor: SendChannel<UserOutgoingMessage>, val username: String) :
    UserActorMsg()

object Completed : UserActorMsg()
data class UserIncomingMessage(val username: String, val message: String, val count: String) : UserActorMsg()
data class UserOutgoingMessage(val author: String, val message: String, var count: String) : UserActorMsg()

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
