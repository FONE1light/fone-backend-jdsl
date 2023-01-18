package com.fone.filmone.domain.profile.entity

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("profile_images")
data class ProfileImage (

    @Id
    var id: Long? = null,

    @Column
    val profileId: Long,

    @Column
    val profileUrl: String,
)