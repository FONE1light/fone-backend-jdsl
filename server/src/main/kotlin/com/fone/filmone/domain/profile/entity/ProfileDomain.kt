package com.fone.filmone.domain.profile.entity

import com.fone.filmone.domain.common.DomainType
import javax.persistence.*

@Entity
@Table(name = "profile_domains")
data class ProfileDomain(

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column
    var profileId: Long,

    @Enumerated(EnumType.STRING)
    var type: DomainType,
) {

    constructor(reqProfileId: Long, domainType: DomainType) : this(
        profileId = reqProfileId,
        type = domainType,
    )
}