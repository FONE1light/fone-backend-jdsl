package com.fone.filmone.domain.profile.entity

import com.fone.filmone.domain.common.Career
import com.fone.filmone.domain.common.Gender
import com.fone.filmone.domain.common.Type
import com.fone.filmone.presentation.profile.RegisterProfileDto
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("profiles")
data class Profile (

    @Id
    var id: Long? = null,

    @Column
    var hookingComment: String,

    @Column
    var birthday: String,

    @Column
    var gender: Gender,

    @Column
    var height: Int,

    @Column
    var weight: Int,

    @Column
    var email: String,

    @Column
    var sns: String,

    @Column
    var specialty: String,

    @Column
    var details: String,

    @Column
    var career: Career,

    @Column
    var interests: String,

    @Column
    var type: Type,

    @Column
    var domains: String,

    @Column
    var userId: Long,

    @Column
    var viewCount: Long,

    @Column
    var isDeleted: Boolean = false,
) {

    fun view() {
        this.viewCount += 1
    }

    fun put(request: RegisterProfileDto.RegisterProfileRequest) {
        hookingComment = request.hookingComment
        birthday = request.birthday.toString()
        gender = request.gender
        height = request.height
        weight = request.weight
        email = request.email
        sns = request.sns
        specialty = request.specialty
        details = request.details
        career = request.career
        interests = request.interests.joinToString(",")
        type = request.type
        domains = request.domains.joinToString(",")
    }

    fun delete() {
        hookingComment = ""
        birthday = ""
        gender = Gender.IRRELEVANT
        height = 0
        weight = 0
        email = ""
        sns = ""
        specialty = ""
        details = ""
        career = Career.IRRELEVANT
        interests = ""
        type = Type.ACTOR
        domains = ""
        isDeleted = true
    }
}