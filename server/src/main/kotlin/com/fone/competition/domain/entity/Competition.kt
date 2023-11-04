package com.fone.competition.domain.entity

import com.fone.common.entity.BaseEntity
import java.time.LocalDate
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "competitions")
class Competition(
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column var title: String,
    @Column(length = 300) var imageUrl: String,
    @Column var screeningStartDate: LocalDate?,
    @Column var screeningEndDate: LocalDate?,
    @Column var exhibitStartDate: LocalDate?,
    @Column var exhibitEndDate: LocalDate?,
    @Column var showStartDate: LocalDate?,
    @Column var agency: String,
    @Column(length = 1500) var details: String,
    @Column var userId: Long,
    @Column var viewCount: Long,
    @Column var scrapCount: Long,
    @Column var linkUrl: String,
) : BaseEntity() {
    fun view() {
        viewCount += 1
    }

    override fun toString(): String {
        return "Competition(id=$id)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Competition

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}
