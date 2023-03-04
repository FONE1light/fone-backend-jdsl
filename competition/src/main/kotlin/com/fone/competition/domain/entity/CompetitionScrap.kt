package com.fone.competition.domain.entity

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fone.common.entity.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "user_competition_scraps")
data class CompetitionScrap(
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column var userId: Long,
    @JsonIgnore
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    var competition: Competition? = null,
) : BaseEntity() {
    constructor(
        reqUserId: Long,
        reqCompetition: Competition,
    ) : this(
        userId = reqUserId,
        competition = reqCompetition
    )
}
