package com.fone.user.domain.enum

enum class Job {
    ACTOR,
    STAFF,
    NORMAL,
    HUNTER,
    ;

    companion object {

        operator fun invoke(job: String) = valueOf(job.uppercase())
    }
}
