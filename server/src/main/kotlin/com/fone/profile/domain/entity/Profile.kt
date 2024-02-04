package com.fone.profile.domain.entity

import com.fone.common.entity.BaseEntity
import com.fone.common.entity.Career
import com.fone.common.entity.ContactMethod
import com.fone.common.entity.Gender
import com.fone.common.entity.Type
import com.fone.profile.presentation.dto.RegisterProfileDto.RegisterProfileRequest
import com.fone.profile.presentation.dto.common.ProfileSnsUrl
import java.time.LocalDate
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.OneToMany
import javax.persistence.Table

@Entity
@Table(name = "profiles")
data class Profile(
    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    // page1
    @Enumerated(EnumType.STRING) var contactMethod: ContactMethod,
    @Column(length = 300) var contact: String,

    // page2
    @Column(length = 10) var name: String,
    @Column var hookingComment: String,
    @OneToMany(
        mappedBy = "profile",
        cascade = [CascadeType.PERSIST, CascadeType.MERGE],
        orphanRemoval = true
    ) var profileImages: MutableList<ProfileImage> = mutableListOf(),
    @Column var representativeImageUrl: String,

    // page3
    @Column var birthday: LocalDate,
    @Enumerated(EnumType.STRING) var gender: Gender?,
    @Column var height: Int?,
    @Column var weight: Int?,
    @Column var email: String,
    @Column(length = 50) var specialty: String,
    @OneToMany(
        cascade = [CascadeType.PERSIST, CascadeType.MERGE],
        orphanRemoval = true
    )
    @JoinColumn(name = "profile_id")
    var snsUrls: Set<ProfileSns>,

    // page4
    @Column(length = 500) var details: String,

    // page5
    @Enumerated(EnumType.STRING) var career: Career,
    @Column(length = 500) var careerDetail: String,

    // etc
    @Enumerated(EnumType.STRING) var type: Type,
    @Column var userId: Long,
    @Column var viewCount: Long = 0,
    @Column var wantCount: Long = 0,
    @Column var isDeleted: Boolean = false,
) : BaseEntity() {

    fun view() {
        this.viewCount += 1
    }

    override fun toString(): String {
        return "Profile(id=$id)"
    }

    fun put(request: RegisterProfileRequest) {
        // page1
        contactMethod = request.firstPage.contactMethod
        contact = request.firstPage.contact

        // page2
        name = request.secondPage.name
        hookingComment = request.secondPage.hookingComment
        representativeImageUrl = request.secondPage.representativeImageUrl
        profileImages = mutableListOf()
        request.secondPage.profileImages.forEach {
            addProfileImage(ProfileImage(it))
        }

        // page3
        birthday = request.thirdPage.birthday
        gender = request.thirdPage.gender
        height = request.thirdPage.height
        weight = request.thirdPage.weight
        email = request.thirdPage.email
        specialty = request.thirdPage.specialty
        snsUrls = request.thirdPage.snsUrls.map(ProfileSnsUrl::toEntity).toSet()

        // page4
        details = request.fourthPage.details

        // page5
        career = request.fifthPage.career
        careerDetail = request.fifthPage.careerDetail

        // page6
        type = request.type
    }

    fun delete() {
        contact = ""
        name = ""
        hookingComment = ""
        representativeImageUrl = ""
        profileImages = mutableListOf()
        gender = null
        height = null
        weight = null
        email = ""
        specialty = ""
        snsUrls = setOf()
        details = ""
        careerDetail = ""
        isDeleted = true
    }

    /* 연관관계 메서드 */
    fun addProfileImage(profileImage: ProfileImage) {
        this.profileImages.add(profileImage)
        profileImage.addProfile(this)
    }
}
