package com.fone.filmone.domain.job_opening.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("works")
data class Work(

    @Id
    var id: Long? = null,

    @Column
    var produce: String,

    @Column
    var title: String,

    @Column
    var director: String,

    @Column
    var genre: String,

    @Column
    var logline: String,

    @Column
    var location: String,

    @Column
    var period: String,

    @Column
    var pay: String,

    @Column
    var details: String,

    @Column
    var manager: String,

    @Column
    var email: String,

    @Column
    var jobOpeningId: Long,

    @Column
    var isDeleted: Boolean = false
) {
    fun delete() {
        produce = ""
        title = ""
        director =""
        genre = ""
        logline = ""
        location = ""
        period = ""
        pay = ""
        details = ""
        manager = ""
        email = ""
        isDeleted = true
    }
}