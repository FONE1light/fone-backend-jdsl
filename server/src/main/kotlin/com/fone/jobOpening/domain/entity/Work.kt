package com.fone.jobOpening.domain.entity

import javax.persistence.Column
import javax.persistence.Embeddable

@Embeddable
data class Work(
    @Column(length = 20) var produce: String,
    @Column(length = 20) var workTitle: String,
    @Column(length = 20) var director: String,
    @Column(length = 20) var genre: String,
    @Column var logline: String?,
    @Column var location: String?,
    @Column var period: String?,
    @Column var pay: String?,
    @Column(length = 500) var details: String,
    @Column(length = 10) var manager: String,
    @Column var email: String,
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
