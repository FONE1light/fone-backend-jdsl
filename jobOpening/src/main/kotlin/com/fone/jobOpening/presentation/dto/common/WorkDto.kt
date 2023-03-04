package com.fone.jobOpening.presentation.dto.common

import com.fone.jobOpening.domain.entity.Work

data class WorkDto(
    val produce: String,
    val workTitle: String,
    val director: String,
    val genre: String,
    val logline: String,
    val location: String,
    val period: String,
    val pay: String,
    val details: String,
    val manager: String,
    val email: String
) {
    constructor(
        work: Work
    ) : this(
        produce = work.produce,
        workTitle = work.workTitle,
        director = work.director,
        genre = work.genre,
        logline = work.logline,
        location = work.location,
        period = work.period,
        pay = work.pay,
        details = work.details,
        manager = work.manager,
        email = work.email
    )

    fun toEntity(): Work {
        return Work(
            produce = produce,
            workTitle = workTitle,
            director = director,
            genre = genre,
            logline = logline,
            location = location,
            period = period,
            pay = pay,
            details = details,
            manager = manager,
            email = email
        )
    }
}
