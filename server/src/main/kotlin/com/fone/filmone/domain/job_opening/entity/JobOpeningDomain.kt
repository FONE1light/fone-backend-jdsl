package com.fone.filmone.domain.job_opening.entity

import javax.persistence.*


@Entity
@Table(name = "job_opening_mains")
data class JobOpeningDomain (

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column
    var jobOpeningId: Long,

    @Column
    var domainId: Long,
)