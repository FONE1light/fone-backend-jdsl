package com.fone.filmone.domain.job_opening.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("works")
data class Work(

    @Id
    var id: Long? = null,

    @Column
    val produce: String,

    @Column
    val title: String,

    @Column
    val director: String,

    @Column
    val genre: String,

    @Column
    val logline: String,

    @Column
    val location: String,

    @Column
    val period: String,

    @Column
    val pay: String,

    @Column
    val details: String,

    @Column
    val manager: String,

    @Column
    val email: String,

    @Column
    val jobOpeningId: Long,
)