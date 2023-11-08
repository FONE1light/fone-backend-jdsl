package com.fone.jobOpening.domain.entity

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
@Table(name = "job_opening_images")
data class JobOpeningImage(
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(length = 300) var jobOpeningUrl: String,
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "job_opening_id")
    var jobOpening: JobOpening? = null,
) : BaseEntity() {

    constructor(url: String) : this(jobOpeningUrl = url)

    override fun toString(): String {
        return "JobOpeningImage(id=$id)"
    }

    fun addJobOpening(jobOpening: JobOpening) {
        this.jobOpening = jobOpening
    }
}
