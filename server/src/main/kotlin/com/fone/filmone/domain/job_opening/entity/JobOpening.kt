package com.fone.filmone.domain.job_opening.entity

import com.fone.filmone.domain.common.Career
import com.fone.filmone.domain.common.Gender
import com.fone.filmone.domain.common.Type
import com.fone.filmone.presentation.job_opening.RegisterJobOpeningDto.RegisterJobOpeningRequest
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table

@Table("job_openings")
data class JobOpening(

    @Id
    var id: Long? = null,

    @Column
    var title: String,

    @Column
    var interests: String,

    @Column
    var deadline: String,

    @Column
    var casting: String,

    @Column
    var numberOfRecruits: Int,

    @Column
    var gender: Gender,

    @Column
    var ageMax: Int,

    @Column
    var ageMin: Int,

    @Column
    var career: Career,

    @Column
    var type: Type,

    @Column
    var domains: String,

    @Column
    var userId: Long,

    @Column
    var viewCount: Long,

    @Column
    var isDeleted: Boolean = false
) {
    fun view() {
        viewCount += 1
    }

    fun put(request: RegisterJobOpeningRequest) {
        title = request.title
        interests = request.interests.joinToString(",")
        deadline = request.deadline.toString()
        casting = request.casting
        numberOfRecruits = request.numberOfRecruits
        gender = request.gender
        ageMax = request.ageMax
        ageMin = request.ageMin
        career = request.career
        type = request.type
        domains = request.domains.joinToString(",")
    }

    fun delete() {
        interests = ""
        deadline = ""
        casting = ""
        numberOfRecruits = 0
        gender = Gender.IRRELEVANT
        ageMax = 0
        ageMin = 0
        career = Career.IRRELEVANT
        type = Type.ACTOR
        domains = ""
        isDeleted = true
    }
}