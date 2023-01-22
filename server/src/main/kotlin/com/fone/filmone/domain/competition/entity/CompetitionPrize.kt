package com.fone.filmone.domain.competition.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("competition_prize")
data class CompetitionPrize (

    @Id
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