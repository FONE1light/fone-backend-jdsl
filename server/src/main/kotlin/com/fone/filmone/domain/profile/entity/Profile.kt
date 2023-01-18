package com.fone.filmone.domain.profile.entity

import com.fone.filmone.domain.common.Career
import com.fone.filmone.domain.common.Gender
import com.fone.filmone.domain.common.Type
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("profiles")
data class Profile (

    @Id
    var id: Long? = null,

    @Column
    val hookingComment: String,

    @Column
    val birthday: String,

    @Column
    val gender: Gender,

    @Column
    val height: Int,

    @Column
    val weight: Int,

    @Column
    val email: String,

    @Column
    val sns: String,

    @Column
    val specialty: String,

    @Column
    val details: String,

    @Column
    val career: Career,

    @Column
    val interests: String,

    @Column
    val type: Type,

    @Column
    val domains: String,

    @Column
    val userId: Long,
)