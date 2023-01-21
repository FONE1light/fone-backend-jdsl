package com.fone.filmone.domain.profile.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table


@Table("user_profile_wants")
data class ProfileWant(

    @Id
    var id: Long? = null,

    @Column
    val userId: Long,

    @Column
    val profileId: Long,
) {

    constructor(reqUserId: Long, reqProfileId: Long) : this(
        userId = reqUserId,
        profileId = reqProfileId,
    )
}