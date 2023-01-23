package com.fone.filmone.domain.profile.entity

import javax.persistence.*

@Entity
@Table(name = "profile_images")
data class ProfileImage (

    @Id
    var id: Long? = null,

    @Column
    val profileId: Long,

    @Column
    val profileUrl: String,
)