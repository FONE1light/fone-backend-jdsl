package com.fone.filmone.domain.job_opening.entity

import com.fone.filmone.common.converter.SeparatorConverter
import com.fone.filmone.domain.common.*
import com.fone.filmone.presentation.job_opening.RegisterJobOpeningDto.RegisterJobOpeningRequest
import javax.persistence.*

@Entity
@Table(name = "job_openings")
data class JobOpening(

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column
    var title: String,

    @Convert(converter = SeparatorConverter::class)
    var interests: List<Interest> = listOf(),

    @Column
    var deadline: String,

    @Column
    var casting: String,

    @Column
    var numberOfRecruits: Int,

    @Enumerated(EnumType.STRING)
    var gender: Gender,

    @Column
    var ageMax: Int,

    @Column
    var ageMin: Int,

    @Enumerated(EnumType.STRING)
    var career: Career,

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

    @Embedded
    var work: Work,
) {
    fun view() {
        viewCount += 1
    }

    fun put(request: RegisterJobOpeningRequest) {
        title = request.title
        interests = request.interests
        deadline = request.deadline.toString()
        casting = request.casting
        numberOfRecruits = request.numberOfRecruits
        gender = request.gender
        ageMax = request.ageMax
        ageMin = request.ageMin
        career = request.career
        type = request.type
        domains = request.domains
    }

    fun delete() {
        work.delete()

        interests = listOf()
        deadline = ""
        casting = ""
        numberOfRecruits = 0
        gender = Gender.IRRELEVANT
        ageMax = 0
        ageMin = 0
        career = Career.IRRELEVANT
        type = Type.ACTOR
        domains = listOf()
        isDeleted = true
    }
}