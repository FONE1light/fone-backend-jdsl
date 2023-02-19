package com.fone.profile.domain.entity

import com.fone.common.entity.BaseEntity
import com.fone.common.entity.Career
import com.fone.common.entity.Gender
import com.fone.common.entity.Type
import com.fone.profile.presentation.RegisterProfileDto.RegisterProfileRequest
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
    var name: String,

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

    @Column(length = 300)
    var sns: String,

    @Column
    var specialty: String,

    @Column(length = 500)
    var details: String,

    @Enumerated(EnumType.STRING)
    var career: Career,

    @Enumerated(EnumType.STRING)
    var type: Type,

    @Column
    var userId: Long,

    @Column
    var viewCount: Long,

    @Column
    var isDeleted: Boolean = false,

    @Column
    var profileUrl: String,

    @OneToMany(mappedBy = "profile", cascade = [CascadeType.PERSIST])
    var profileImages: MutableList<ProfileImage> = mutableListOf(),
) : BaseEntity() {

    fun view() {
        this.viewCount += 1
    }

    override fun toString(): String {
        return "Profile(id=$id)"
    }

    fun put(request: RegisterProfileRequest) {
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
        type = request.type
        profileUrl = request.profileUrl
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
        type = Type.ACTOR
        isDeleted = true
    }

    /* 연관관계 메서드 */
    fun addProfileImage(profileImage: ProfileImage) {
        this.profileImages.add(profileImage)
        profileImage.addProfile(this)
    }
}