package com.fone.profile.domain.entity

import com.fone.common.entity.BaseEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "user_profile_wants")
data class ProfileWant(
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column val userId: Long,
    @Column val profileId: Long,
) : BaseEntity() {

    constructor(
        reqUserId: Long,
        reqProfileId: Long,
    ) : this(
        userId = reqUserId,
        profileId = reqProfileId
    )
}
