package com.fone.jobOpening.domain.entity

import com.fone.common.converter.SeparatorConverter
import com.fone.common.entity.BaseEntity
import com.fone.common.entity.ContactMethod
import com.fone.common.entity.Gender
import com.fone.common.entity.Salary
import com.fone.common.entity.Type
import com.fone.jobOpening.infrastructure.toLocation
import com.fone.jobOpening.presentation.dto.RegisterJobOpeningRequest
import java.time.LocalDate
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Convert
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "job_openings")
data class JobOpening(
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    // page1
    @Enumerated(EnumType.STRING) var contactMethod: ContactMethod,
    @Column(length = 300) var contact: String,

    // page2
    @Column var title: String,
    @Column var recruitmentStartDate: LocalDate?,
    @Column var recruitmentEndDate: LocalDate?,
    @OneToMany(mappedBy = "jobOpening", cascade = [CascadeType.PERSIST, CascadeType.MERGE], orphanRemoval = true)
    var imageUrls: MutableList<JobOpeningImage> = mutableListOf(),
    @Column(length = 300) var representativeImageUrl: String?,

    // page3
    @Column(length = 50) var casting: String?,
    @Column var numberOfRecruits: Int,
    @Enumerated(EnumType.STRING) var gender: Gender,
    @Column var ageMax: Int,
    @Column var ageMin: Int,
    @Convert(converter = SeparatorConverter::class)
    var careers: List<String> = listOf(),

    // page4
    @Column(length = 20) var produce: String,
    @Column(length = 20) var workTitle: String,
    @Column(length = 20) var director: String,
    @Convert(converter = SeparatorConverter::class)
    var genres: List<String> = listOf(),
    @Column var logline: String?,

    // page5
    @OneToOne
    var location: Location,
    @Column var workingStartDate: LocalDate?,
    @Column var workingEndDate: LocalDate?,
    @Convert(converter = SeparatorConverter::class)
    var selectedDays: List<String> = listOf(),
    @Column var workingStartTime: String?,
    @Column var workingEndTime: String?,
    @Enumerated(EnumType.STRING) var salaryType: Salary,
    @Column var salary: Int,

    // page6
    @Column(length = 500) var details: String,

    // page7
    @Column(length = 10) var manager: String,
    @Column var email: String,

    // etc
    @Enumerated(EnumType.STRING) var type: Type,
    @Column var userId: Long,
    @Column var viewCount: Long = 0,
    @Column var scrapCount: Long = 0,
    @Column var isDeleted: Boolean = false,
) : BaseEntity() {
    fun view() {
        viewCount += 1
    }

    suspend fun put(request: RegisterJobOpeningRequest) {
        contactMethod = request.firstPage.contactMethod
        contact = request.firstPage.contact

        title = request.secondPage.title
        recruitmentStartDate = request.secondPage.recruitmentStartDate
        recruitmentEndDate = request.secondPage.recruitmentEndDate
        representativeImageUrl = request.secondPage.representativeImageUrl
        imageUrls.clear()
        request.secondPage.imageUrls.forEach {
            addJobOpeningImage(JobOpeningImage(it))
        }

        casting = request.thirdPage.casting
        numberOfRecruits = request.thirdPage.numberOfRecruits
        gender = request.thirdPage.gender
        ageMax = request.thirdPage.ageMax ?: 200
        ageMin = request.thirdPage.ageMin ?: 0
        careers = request.thirdPage.careers.map { it.toString() }

        produce = request.fourthPage.produce
        workTitle = request.fourthPage.workTitle
        director = request.fourthPage.director
        genres = request.fourthPage.genres.map { it.toString() }
        logline = request.fourthPage.logline

        location = request.fifthPage.toLocation()
        workingStartDate = request.fifthPage.workingStartDate
        workingEndDate = request.fifthPage.workingEndDate
        selectedDays = request.fifthPage.selectedDays.map { it.toString() }
        workingStartTime = request.fifthPage.workingStartTime
        workingEndTime = request.fifthPage.workingEndTime
        salaryType = request.fifthPage.salaryType
        salary = request.fifthPage.salary

        details = request.sixthPage.details

        manager = request.seventhPage.manager
        email = request.seventhPage.email
    }

    fun delete() {
        contact = ""
        title = ""
        recruitmentStartDate = null
        recruitmentEndDate = null
        representativeImageUrl = ""
        imageUrls.clear()
        casting = ""
        numberOfRecruits = -1
        careers = listOf()
        produce = ""
        workTitle = ""
        director = ""
        genres = listOf()
        logline = ""
        workingStartDate = null
        workingEndDate = null
        selectedDays = listOf()
        workingStartTime = ""
        workingEndTime = ""
        salary = -1
        details = ""
        manager = ""
        email = ""
        isDeleted = true
    }

    // 연관관계 메서드
    fun addJobOpeningImage(jobOpeningImage: JobOpeningImage) {
        this.imageUrls.add(jobOpeningImage)
        jobOpeningImage.addJobOpening(this)
    }
}
