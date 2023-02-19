package com.fone.competition.domain.entity

import com.fone.common.entity.BaseEntity
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