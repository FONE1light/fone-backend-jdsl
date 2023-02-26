package com.fone.jobOpening.domain.entity

import com.fone.common.entity.DomainType
import javax.persistence.*

@Entity
@Table(name = "job_opening_domains")
data class JobOpeningDomain(
    @Id @Column @GeneratedValue(strategy = GenerationType.IDENTITY) var id: Long? = null,
    @Column var jobOpeningId: Long,
    @Enumerated(EnumType.STRING) var type: DomainType,
) {

    constructor(
        reqJobOpeningId: Long,
        reqType: DomainType
    ) : this(
        jobOpeningId = reqJobOpeningId,
        type = reqType,
    )
}
