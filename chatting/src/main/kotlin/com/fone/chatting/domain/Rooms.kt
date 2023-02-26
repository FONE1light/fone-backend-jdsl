package com.fone.chatting.domain

import com.fone.chatting.domain.actor.RoomActorMsg
import com.fone.chatting.domain.actor.roomActor
import java.util.concurrent.ConcurrentHashMap
import kotlinx.coroutines.channels.SendChannel

object Rooms {
    private val rooms: ConcurrentHashMap<Int, SendChannel<RoomActorMsg>> = ConcurrentHashMap()

    fun findOrCreate(roomId: Int): SendChannel<RoomActorMsg> =
        rooms[roomId] ?: createNewRoom(roomId)

    private fun createNewRoom(roomId: Int) =
        roomActor(roomId).also { actor -> rooms[roomId] = actor }
}
