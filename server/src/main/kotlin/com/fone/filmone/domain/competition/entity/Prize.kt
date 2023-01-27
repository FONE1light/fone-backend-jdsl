package com.fone.filmone.domain.competition.entity

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

    @Column
    var agency: String,

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "competition_id")
    var competition: Competition? = null,
) {

    override fun toString(): String {
        return "Prize(id=$id)"
    }

    fun addCompetition(competition: Competition) {
        this.competition = competition
    }
}