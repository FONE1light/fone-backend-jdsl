package com.fone.jobOpening.domain.entity

import com.fone.common.entity.DomainType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "job_opening_domains")
data class JobOpeningDomain(
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column var jobOpeningId: Long,
    @Enumerated(EnumType.STRING) var type: DomainType
) {

    constructor(
        reqJobOpeningId: Long,
        reqType: DomainType
    ) : this(
        jobOpeningId = reqJobOpeningId,
        type = reqType
    )
}
