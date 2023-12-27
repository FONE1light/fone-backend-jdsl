package com.fone.profile.presentation.dto

import com.fone.common.entity.Career
import com.fone.common.entity.CategoryType
import com.fone.common.entity.DomainType
import com.fone.common.entity.Gender
import com.fone.common.entity.Type
import com.fone.profile.domain.entity.Profile
import com.fone.profile.domain.entity.ProfileWant
import com.fone.profile.presentation.dto.common.ProfileDto
import com.fone.profile.presentation.dto.common.ProfileSnsUrl
import com.fone.user.domain.enum.Job
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import javax.validation.constraints.Email

class RegisterProfileDto {
    data class RegisterProfileRequest(
        @field:Schema(description = "프로필 이름", required = true, example = "차이나는 클라스")
        val name: String,
        @field:Schema(description = "후킹멘트", required = true, example = "제가 좋아하는 색은 노랑색이에요")
        val hookingComment: String,
        @field:Schema(description = "생년월일", required = true, example = "2000-10-01")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        val birthday: LocalDate,
        @field:Schema(description = "성별", example = "WOMAN", required = true)
        val gender: Gender,
        @field:Schema(description = "키", example = "188", required = true)
        val height: Int,
        @field:Schema(description = "몸무게", example = "70", required = true)
        val weight: Int,
        @field:Email(message = "유효하지 않는 이메일 입니다.")
        @field:Schema(
            description = "이메일",
            example = "test@test.com",
            required = true
        )
        val email: String,
        @field:Schema(description = "SNS url v1", example = "https://www.youtube.com/channel")
        val sns: String? = null,
        @field:Schema(description = "SNS url v2")
        val snsUrls: List<ProfileSnsUrl> = listOf(),
        @field:Schema(description = "특기", required = true, example = "매운 음식 먹기")
        val specialty: String,
        @field:Schema(description = "상세요강", required = true)
        val details: String,
        @field:Schema(description = "경력", required = true, example = "LESS_THAN_10YEARS")
        val career: Career,
        @field:Schema(description = "경력 상세 설명", example = "복숭아 요거트 제작 3년")
        val careerDetail: String?, // 하위 버전 호환성을 위해 null타입 추가 필요
        @field:Schema(description = "관심사", example = "[\"YOUTUBE\"]", required = true)
        val categories: List<CategoryType>,
        @field:Schema(description = "타입", required = true)
        val type: Type,
        @field:Schema(description = "분야", example = "[\"PLANNING\"]")
        val domains: List<DomainType>?,
        @field:Schema(
            description = "이미지 URL",
            example = "['https://s3-ap-northeast-2.amazonaws.com/f-one-image/prod/user-profile/image.jpg']"
        )
        val profileImages: List<String>,
        @field:Schema(
            description = "대표 이미지 URL",
            example = "https://s3-ap-northeast-2.amazonaws.com/f-one-image/prod/user-profile/image.jpg"
        )
        val representativeImageUrl: String,
    ) {
        fun toEntity(userId: Long): Profile {
            return Profile(
                hookingComment = hookingComment,
                birthday = birthday,
                gender = gender,
                height = height,
                weight = weight,
                email = email,
                sns = sns ?: "",
                snsUrls = snsUrls.map(ProfileSnsUrl::toEntity).toSet(),
                specialty = specialty,
                details = details,
                career = career,
                careerDetail = careerDetail ?: "",
                type = type,
                userId = userId,
                viewCount = 0,
                name = name,
                representativeImageUrl = representativeImageUrl
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
            profile =
            ProfileDto(
                profile,
                userProfileWantMap,
                profile.profileImages,
                domains,
                categories,
                nickname,
                profileUrl,
                job
            )
        )
    }
}
