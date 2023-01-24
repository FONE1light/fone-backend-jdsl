package com.fone.filmone.domain.profile.entity

import javax.persistence.*


@Entity
@Table(name = "user_profile_wants")
data class ProfileWant(

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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