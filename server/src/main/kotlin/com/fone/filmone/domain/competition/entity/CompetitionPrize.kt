package com.fone.filmone.domain.competition.entity

import javax.persistence.*

@Entity
@Table(name = "competition_prizes")
data class CompetitionPrize (

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column
    var ranking: String,

    @Column
    var prizeMoney: String,

    @Column
    var agency: String,

    @Column
    var competitionId: Long,
)