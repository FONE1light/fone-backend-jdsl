package com.fone.filmone.domain.competition.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table


@Table("user_competition_scraps")
data class CompetitionScrap (

    @Id
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