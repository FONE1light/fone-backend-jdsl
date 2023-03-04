package com.fone.competition.domain.entity

import com.fone.common.entity.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType.LAZY
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.Table

@Entity
@Table(name = "competition_prizes")
data class Prize(
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column var ranking: String,
    @Column var prizeMoney: String,
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "competition_id")
    var competition: Competition? = null,
) : BaseEntity() {

    override fun toString(): String {
        return "Prize(id=$id)"
    }

    fun addCompetition(competition: Competition) {
        this.competition = competition
    }
}
