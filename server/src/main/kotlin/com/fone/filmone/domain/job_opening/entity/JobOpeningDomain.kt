package com.fone.filmone.domain.job_opening.entity

import com.fone.filmone.domain.common.DomainType
import javax.persistence.*


@Entity
@Table(name = "job_opening_domains")
data class JobOpeningDomain(

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column
    var jobOpeningId: Long,

    @Enumerated(EnumType.STRING)
    var type: DomainType,
) {

    constructor(reqJobOpeningId: Long, reqType: DomainType) : this(
        jobOpeningId = reqJobOpeningId,
        type = reqType,
    )
}