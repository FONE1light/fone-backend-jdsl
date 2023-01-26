package com.fone.filmone.domain.job_opening.entity

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class Work(
    @Column
    var produce: String,

    @Column
    var workTitle: String,

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
) {
    fun delete() {
        produce = ""
        workTitle = ""
        director = ""
        genre = ""
        logline = ""
        location = ""
        period = ""
        pay = ""
        details = ""
        manager = ""
        email = ""
    }
}