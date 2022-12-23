package com.fone.filmone.domain.user.enum

enum class Job {
    ACTOR, STAFF, NORMAL, HUNTER;

    companion object {

        operator fun invoke(priority: String) = valueOf(priority.uppercase())
    }
}