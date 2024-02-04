package com.fone.profile.presentation.dto.common

import com.fone.common.entity.CategoryType
import com.fone.common.entity.DomainType
import com.fone.common.entity.Type
import com.fone.common.utils.DateTimeFormat
import com.fone.jobOpening.presentation.dto.ValidateJobOpeningDto
import com.fone.profile.domain.entity.Profile
import com.fone.profile.domain.entity.ProfileImage
import com.fone.profile.domain.entity.ProfileSns
import com.fone.profile.domain.entity.ProfileWant
import com.fone.profile.presentation.dto.ValidateProfileDto
import com.fone.user.domain.enum.Job
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDateTime

data class ProfileDto(
    @Schema(description = "id")
    val id: Long,

    @Schema(description = "1번째 페이지")
    val firstPage: ValidateJobOpeningDto.FirstPage,

    @Schema(description = "2번째 페이지")
    val secondPage: ValidateProfileDto.SecondPage,

    @Schema(description = "3번째 페이지")
    val thirdPage: ValidateProfileDto.ThirdPage,

    @Schema(description = "4번째 페이지")
    val fourthPage: ValidateProfileDto.FourthPage,

    @Schema(description = "5번째 페이지")
    val fifthPage: ValidateProfileDto.FifthPage,

    @Schema(description = "6번째 페이지")
    val sixthPage: ValidateProfileDto.SixthPage,

    @Schema(description = "타입")
    val type: Type,

    @Schema(description = "조회수")
    val viewCount: Long,
    @Schema(description = "찜 여부")
    val isWant: Boolean = false,
    @Schema(description = "생성 시간")
    val createdAt: LocalDateTime,
    @Schema(description = "닉네임")
    val userNickname: String,
    @Schema(
        description = "유저 이미지 URL",
        example = "https://s3-ap-northeast-2.amazonaws.com/f-one-image/prod/user-profile/image.jpg"
    )
    val userProfileUrl: String,
    @Schema(description = "Job 타입")
    val userJob: Job,
) {
    @get:Schema(description = "나이", example = "45")
    val age: Int
        get() = DateTimeFormat.calculateAge(thirdPage.birthday)

    constructor(
        profile: Profile,
        userProfileWantMap: Map<Long, ProfileWant?>,
        profileImages: List<ProfileImage>,
        domains: List<DomainType>?,
        categories: List<CategoryType>,
        userNickname: String,
        profileUrl: String,
        job: Job,
    ) : this(
        id = profile.id ?: 0L,
        firstPage = ValidateJobOpeningDto.FirstPage(
            contactMethod = profile.contactMethod,
            contact = profile.contact
        ),
        secondPage = ValidateProfileDto.SecondPage(
            name = profile.name,
            hookingComment = profile.hookingComment,
            profileImages = profileImages.map { it.url },
            representativeImageUrl = profile.representativeImageUrl
        ),
        thirdPage = ValidateProfileDto.ThirdPage(
            birthday = profile.birthday,
            gender = profile.gender,
            height = profile.height,
            weight = profile.weight,
            email = profile.email,
            domains = domains,
            specialty = profile.specialty,
            snsUrls = profile.snsUrls.map(ProfileSns::toDto)
        ),
        fourthPage = ValidateProfileDto.FourthPage(
            details = profile.details
        ),
        fifthPage = ValidateProfileDto.FifthPage(
            career = profile.career,
            careerDetail = profile.careerDetail
        ),
        sixthPage = ValidateProfileDto.SixthPage(
            categories = categories
        ),
        type = profile.type,
        viewCount = profile.viewCount,
        isWant = userProfileWantMap[profile.id] != null,
        createdAt = profile.createdAt,
        userNickname = userNickname,
        userProfileUrl = profileUrl,
        userJob = job
    )
}
