package com.fone.filmone.domain.job_opening.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("user_job_opening_scraps")
data class JobOpeningScrap (

    @Id
    var id: Long? = null,

    @Column
    val userId: Long,

    @Column
    val jobOpeningId: Long,
) {
    constructor(reqUserId: Long, reqJobOpeningId: Long) : this(
        userId = reqUserId,
        jobOpeningId = reqJobOpeningId
    )
}