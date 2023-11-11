package com.fone.profile.presentation.dto.common

import com.fone.common.entity.Career
import com.fone.common.entity.CategoryType
import com.fone.common.entity.DomainType
import com.fone.common.entity.Gender
import com.fone.common.entity.Type
import com.fone.common.utils.DateTimeFormat
import com.fone.profile.domain.entity.Profile
import com.fone.profile.domain.entity.ProfileImage
import com.fone.profile.domain.entity.ProfileSns
import com.fone.profile.domain.entity.ProfileWant
import com.fone.user.domain.enum.Job
import io.swagger.v3.oas.annotations.media.Schema
import java.time.LocalDate
import java.time.LocalDateTime

data class ProfileDto(
    @Schema(description = "id")
    val id: Long,
    @Schema(description = "프로필 이름")
    val name: String,
    @Schema(description = "후킹멘트")
    val hookingComment: String,
    @Schema(description = "생년월일")
    val birthday: LocalDate?,
    @Schema(description = "성별", example = "WOMAN")
    val gender: Gender,
    @Schema(description = "키", example = "188")
    val height: Int,
    @Schema(description = "몸무게", example = "70")
    val weight: Int,
    @Schema(description = "이메일", example = "example@something.com")
    val email: String,
    @Schema(description = "SNS v1", example = "https://www.youtube.com/channel")
    val sns: String,
    @Schema(description = "SNS v2")
    val snsUrls: List<ProfileSnsUrl>,
    @Schema(description = "특기")
    val specialty: String,
    @Schema(description = "상세요강")
    val details: String,
    @Schema(description = "타입")
    val type: Type,
    @Schema(description = "경력", example = "LESS_THAN_3YEARS")
    val career: Career,
    @Schema(description = "경력 상세 설명")
    val careerDetail: String,
    @Schema(description = "관심사", example = "WEB_DRAMA")
    val categories: List<CategoryType>,
    @Schema(description = "분야", example = "PLANNING")
    val domains: List<DomainType>?,
    @Schema(
        description = "(Deprecated) 이미지 URL",
        deprecated = true,
        example = "['https://s3-ap-northeast-2.amazonaws.com/f-one-image/prod/user-profile/image.jpg']"
    )
    @Deprecated("profileImages으로 대체됩니다.")
    val profileUrls: List<String>,
    @Schema(
        description = "이미지 URL",
        example = "['https://s3-ap-northeast-2.amazonaws.com/f-one-image/prod/user-profile/image.jpg']"
    )
    val profileImages: List<String>,
    @Schema(description = "조회수")
    val viewCount: Long,
    @Schema(
        description = "(Deprecated) 대표 이미지 URL",
        deprecated = true,
        example = "https://s3-ap-northeast-2.amazonaws.com/f-one-image/prod/user-profile/image.jpg"
    )
    @Deprecated("representativeImageUrl으로 대체됩니다.")
    val profileUrl: String,
    @Schema(
        description = "대표 이미지 URL",
        example = "https://s3-ap-northeast-2.amazonaws.com/f-one-image/prod/user-profile/image.jpg"
    )
    val representativeImageUrl: String,
    @Schema(description = "찜 여부")
    val isWant: Boolean = false,
    @Schema(description = "나이", example = "45")
    val age: Int,
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
        id = profile.id!!,
        name = profile.name,
        hookingComment = profile.hookingComment,
        birthday = profile.birthday,
        gender = profile.gender,
        height = profile.height,
        weight = profile.weight,
        email = profile.email,
        sns = profile.sns,
        snsUrls = profile.snsUrls.map(ProfileSns::toDto),
        type = profile.type,
        specialty = profile.specialty,
        details = profile.details,
        career = profile.career,
        careerDetail = profile.careerDetail,
        categories = categories,
        domains = domains,
        viewCount = profile.viewCount,
        isWant = userProfileWantMap[profile.id!!] != null,
        age = DateTimeFormat.calculateAge(profile.birthday),
        profileUrl = profile.profileUrl,
        profileUrls = profileImages.map { it.profileUrl },
        representativeImageUrl = profile.representativeImageUrl,
        profileImages = profileImages.map { it.url },
        createdAt = profile.createdAt,
        userNickname = userNickname,
        userProfileUrl = profileUrl,
        userJob = job
    )
}
