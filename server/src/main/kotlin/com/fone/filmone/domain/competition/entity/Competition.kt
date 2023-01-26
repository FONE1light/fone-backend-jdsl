package com.fone.filmone.domain.competition.entity

import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "competitions")
data class Competition(

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column
    var title: String,

    @Column(length = 300)
    var imageUrl: String,

    @Column
    var startDate: LocalDate?,

    @Column
    var endDate: LocalDate?,

    @Column
    var agency: String,

    @Column(length = 500)
    var details: String,

    @Column
    var userId: Long,

    @Column
    var viewCount: Long,

    @OneToMany(mappedBy = "competition", cascade = [CascadeType.PERSIST])
    val prizes: MutableList<Prize> = mutableListOf(),
) {
    fun view() {
        viewCount += 1
    }

    override fun toString(): String {
        return "Competition(id=$id)"
    }

    /* 연관관계 메서드 */
    fun addPrize(prize: Prize) {
        this.prizes.add(prize)
        prize.addCompetition(this)
    }
}