package com.fone.filmone.domain.profile.entity

import com.fone.filmone.common.converter.SeparatorConverter
import com.fone.filmone.domain.common.*
import com.fone.filmone.presentation.profile.RegisterProfileDto
import java.time.LocalDate
import javax.persistence.*

@Entity
@Table(name = "profiles")
data class Profile(

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column
    var hookingComment: String,

    @Column
    var birthday: LocalDate?,

    @Enumerated(EnumType.STRING)
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

    @Enumerated(EnumType.STRING)
    var career: Career,

    @Convert(converter = SeparatorConverter::class)
    var interests: List<Interest> = listOf(),

    @Enumerated(EnumType.STRING)
    var type: Type,

    @Convert(converter = SeparatorConverter::class)
    var domains: List<Domain> = listOf(),

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
        birthday = request.birthday
        gender = request.gender
        height = request.height
        weight = request.weight
        email = request.email
        sns = request.sns
        specialty = request.specialty
        details = request.details
        career = request.career
        interests = request.interests
        type = request.type
        domains = request.domains
    }

    fun delete() {
        hookingComment = ""
        birthday = null
        gender = Gender.IRRELEVANT
        height = 0
        weight = 0
        email = ""
        sns = ""
        specialty = ""
        details = ""
        career = Career.IRRELEVANT
        interests = listOf()
        type = Type.ACTOR
        domains = listOf()
        isDeleted = true
    }
}