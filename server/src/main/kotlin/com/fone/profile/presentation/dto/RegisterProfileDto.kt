package com.fone.profile.presentation.dto

import com.fone.common.entity.Career
import com.fone.common.entity.CategoryType
import com.fone.common.entity.DomainType
import com.fone.common.entity.Gender
import com.fone.common.entity.Type
import com.fone.profile.domain.entity.Profile
import com.fone.profile.domain.entity.ProfileWant
import com.fone.profile.presentation.dto.common.ProfileDto
import com.fone.user.domain.enum.Job
import io.swagger.annotations.ApiModelProperty
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import javax.validation.constraints.Email

class RegisterProfileDto {

    data class RegisterProfileRequest(
        @ApiModelProperty(value = "프로필 이름")
        val name: String,
        @ApiModelProperty(value = "후킹멘트")
        val hookingComment: String,
        @ApiModelProperty(value = "생년월일")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        val birthday: LocalDate,
        @ApiModelProperty(value = "성별")
        val gender: Gender,
        @ApiModelProperty(value = "키")
        val height: Int,
        @ApiModelProperty(value = "몸무게")
        val weight: Int,
        @field:Email(message = "유효하지 않는 이메일 입니다.")
        @ApiModelProperty(
            value = "이메일",
            example = "test@test.com",
            required = true
        )
        val email: String,
        @ApiModelProperty(value = "SNS url v1")
        val sns: String? = null,
        @ApiModelProperty(value = "SNS url v2")
        val snsUrls: List<String> = listOf(),
        @ApiModelProperty(value = "특기")
        val specialty: String,
        @ApiModelProperty(value = "상세요강")
        val details: String,
        @ApiModelProperty(value = "경력")
        val career: Career,
        @ApiModelProperty(value = "경력 상세 설명")
        val careerDetail: String?, // 하위 버전 호환성을 위해 null타입 추가 필요
        @ApiModelProperty(value = "관심사")
        val categories: List<CategoryType>,
        @ApiModelProperty(value = "타입")
        val type: Type,
        @ApiModelProperty(value = "분야")
        val domains: List<DomainType>?,
        @ApiModelProperty(value = "이미지 URL")
        val profileUrls: List<String>,
        @ApiModelProperty(value = "대표 이미지 URL")
        val profileUrl: String,
    ) {

        fun toEntity(userId: Long): Profile {
            return Profile(
                hookingComment = hookingComment,
                birthday = birthday,
                gender = gender,
                height = height,
                weight = weight,
                email = email,
                sns = if (sns.isNullOrBlank()) snsUrls else listOf(sns),
                specialty = specialty,
                details = details,
                career = career,
                careerDetail = careerDetail ?: "",
                type = type,
                userId = userId,
                viewCount = 0,
                name = name,
                profileUrl = profileUrl
            )
        }
    }

    data class RegisterProfileResponse(
        val profile: ProfileDto,
    ) {

        constructor(
            profile: Profile,
            userProfileWantMap: Map<Long, ProfileWant?>,
            domains: List<DomainType>?,
            categories: List<CategoryType>,
            nickname: String,
            profileUrl: String,
            job: Job,
        ) : this(
            profile = ProfileDto(
                profile,
                userProfileWantMap,
                profile.profileImages.map { it.profileUrl }.toList(),
                domains,
                categories,
                nickname,
                profileUrl,
                job
            )
        )
    }
}
