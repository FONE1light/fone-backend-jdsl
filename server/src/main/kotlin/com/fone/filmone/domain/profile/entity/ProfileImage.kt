package com.fone.filmone.domain.profile.entity

import javax.persistence.*

@Entity
@Table(name = "profile_images")
data class ProfileImage (

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column
    val profileId: Long,

    @Column(length = 300)
    val profileUrl: String,
)