package com.fone.filmone.domain.competition.entity

import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "competitions")
data class Competition(

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column
    var title: String,

    @Column
    var imageUrl: String,

    @Column
    var startDate: LocalDate?,

    @Column
    var endDate: LocalDate?,

    @Column
    var agency: String,

    @Column
    var details: String,

    @Column
    var userId: Long,

    @Column
    var viewCount: Long,
) {
    fun view() {
        viewCount += 1
    }
}