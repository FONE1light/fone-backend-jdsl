package com.fone.jobOpening.domain.entity

import com.fone.common.entity.BaseEntity
import com.fone.common.entity.Career
import com.fone.common.entity.Gender
import com.fone.common.entity.Type
import com.fone.jobOpening.presentation.dto.RegisterJobOpeningDto.RegisterJobOpeningRequest
import java.time.LocalDate
import javax.persistence.Column
import javax.persistence.Embedded
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "job_openings")
data class JobOpening(
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    @Column var title: String,
    @Column var deadline: LocalDate?,
    @Column var casting: String,
    @Column var numberOfRecruits: Int,
    @Enumerated(EnumType.STRING) var gender: Gender,
    @Column var ageMax: Int,
    @Column var ageMin: Int,
    @Enumerated(EnumType.STRING) var career: Career,
    @Enumerated(EnumType.STRING) var type: Type,
    @Column var userId: Long,
    @Column var viewCount: Long,
    @Column var scrapCount: Long,
    @Column var isDeleted: Boolean = false,
    @Embedded var work: Work
) : BaseEntity() {
    fun view() {
        viewCount += 1
    }

    fun put(request: RegisterJobOpeningRequest) {
        title = request.title
        deadline = request.deadline
        casting = request.casting
        numberOfRecruits = request.numberOfRecruits
        gender = request.gender
        ageMax = request.ageMax
        ageMin = request.ageMin
        career = request.career
        type = request.type
    }

    fun delete() {
        work.delete()

        deadline = null
        casting = ""
        numberOfRecruits = 0
        gender = Gender.IRRELEVANT
        ageMax = 0
        ageMin = 0
        career = Career.IRRELEVANT
        type = Type.ACTOR
        isDeleted = true
    }
}
