package com.fone.filmone.domain.competition.entity

import javax.persistence.*
import javax.persistence.FetchType.*

@Entity
@Table(name = "competition_prizes")
data class Prize (

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

    @ManyToOne(fetch = EAGER)
    @JoinColumn(name = "competition_id")
    var competition: Competition? = null,
) {

    fun addCompetition(competition: Competition) {
        this.competition = competition
    }
}