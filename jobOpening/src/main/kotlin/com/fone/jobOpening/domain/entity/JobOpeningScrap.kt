package com.fone.jobOpening.domain.entity

import com.fone.common.entity.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "user_job_opening_scraps")
data class JobOpeningScrap(
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column val userId: Long,
    @Column val jobOpeningId: Long
) : BaseEntity() {
    constructor(
        reqUserId: Long,
        reqJobOpeningId: Long
    ) : this(userId = reqUserId, jobOpeningId = reqJobOpeningId)
}
