package com.fone.profile.presentation.dto.common

import com.fone.common.entity.Career
import com.fone.common.entity.CategoryType
import com.fone.common.entity.DomainType
import com.fone.common.entity.Gender
import com.fone.common.entity.Type
import com.fone.common.utils.DateTimeFormat
import com.fone.profile.domain.entity.Profile
import com.fone.profile.domain.entity.ProfileSns
import com.fone.profile.domain.entity.ProfileWant
import com.fone.user.domain.enum.Job
import io.swagger.annotations.ApiModelProperty
import java.time.LocalDate
import java.time.LocalDateTime

data class ProfileDto(
    @ApiModelProperty(value = "id")
    val id: Long,
    @ApiModelProperty(value = "프로필 이름")
    val name: String,
    @ApiModelProperty(value = "후킹멘트")
    val hookingComment: String,
    @ApiModelProperty(value = "생년월일")
    val birthday: LocalDate?,
    @ApiModelProperty(value = "성별")
    val gender: Gender,
    @ApiModelProperty(value = "키")
    val height: Int,
    @ApiModelProperty(value = "몸무게")
    val weight: Int,
    @ApiModelProperty(value = "이메일")
    val email: String,
    @ApiModelProperty(value = "SNS v1")
    val sns: String,
    @ApiModelProperty(value = "SNS v2")
    val snsUrls: List<ProfileSnsUrl>,
    @ApiModelProperty(value = "특기")
    val specialty: String,
    @ApiModelProperty(value = "상세요강")
    val details: String,
    @ApiModelProperty(value = "타입")
    val type: Type,
    @ApiModelProperty(value = "경력")
    val career: Career,
    @ApiModelProperty(value = "경력 상세 설명")
    val careerDetail: String,
    @ApiModelProperty(value = "관심사")
    val categories: List<CategoryType>,
    @ApiModelProperty(value = "분야")
    val domains: List<DomainType>?,
    @ApiModelProperty(value = "이미지 URL")
    val profileUrls: List<String>,
    @ApiModelProperty(value = "조회수")
    val viewCount: Long,
    val profileUrl: String,
    @ApiModelProperty(value = "찜 여부")
    val isWant: Boolean = false,
    @ApiModelProperty(value = "나이")
    val age: Int,
    @ApiModelProperty(value = "생성 시간")
    val createdAt: LocalDateTime,
    @ApiModelProperty(value = "닉네임")
    val userNickname: String,
    @ApiModelProperty(value = "대표 이미지 URL")
    val userProfileUrl: String,
    @ApiModelProperty(value = "Job 타입")
    val userJob: Job,
) {
    constructor(
        profile: Profile,
        userProfileWantMap: Map<Long, ProfileWant?>,
        profileUrls: List<String>,
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
        profileUrls = profileUrls,
        viewCount = profile.viewCount,
        isWant = userProfileWantMap[profile.id!!] != null,
        age = DateTimeFormat.calculateAge(profile.birthday),
        profileUrl = if (profileUrls.isEmpty()) "" else profileUrls[0],
        createdAt = profile.createdAt,
        userNickname = userNickname,
        userProfileUrl = profileUrl,
        userJob = job
    )
}
