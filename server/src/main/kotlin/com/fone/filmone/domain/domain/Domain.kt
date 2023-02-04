package com.fone.filmone.domain.domain

import com.fone.filmone.domain.common.DomainType
import javax.persistence.*

@Entity
@Table(name = "domains")
data class Domain(

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Enumerated(EnumType.STRING)
    var type: DomainType,
)