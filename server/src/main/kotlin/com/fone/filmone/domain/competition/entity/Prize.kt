package com.fone.filmone.domain.competition.entity

import com.fone.filmone.domain.common.BaseEntity
import javax.persistence.*
import javax.persistence.FetchType.LAZY

@Entity
@Table(name = "competition_prizes")
data class Prize(

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column
    var ranking: String,

    @Column
    var prizeMoney: String,

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