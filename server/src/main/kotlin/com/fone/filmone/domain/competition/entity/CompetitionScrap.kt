package com.fone.filmone.domain.competition.entity

import javax.persistence.*


@Entity
@Table(name = "user_competition_scraps")
data class CompetitionScrap (

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column
    var userId: Long,

    @Column
    var competitionId: Long,
) {
    constructor(reqUserId: Long, reqCompetitionId: Long) : this(
        userId = reqUserId,
        competitionId = reqCompetitionId,
    )
}