package com.fone.filmone.presentation.profile

import com.fone.filmone.domain.common.*
import com.fone.filmone.domain.profile.entity.Profile
import com.fone.filmone.domain.profile.entity.ProfileWant
import io.swagger.annotations.ApiModelProperty
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import javax.validation.constraints.Email

class RegisterProfileDto {

    data class RegisterProfileRequest(
        val hookingComment: String,
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        val birthday: LocalDate,
        val gender: Gender,
        val height: Int,
        val weight: Int,
        @field:Email(message = "유효하지 않는 이메일 입니다.")
        @ApiModelProperty(value = "이메일", example = "test@test.com", required = true)
        val email: String,
        val sns: String,
        val specialty: String,
        val details: String,
        val career: Career,
        val interests: List<Interest>,
        val type: Type,
        val domains: List<Domain>,
        val userId: Long,
        val profileUrls: List<String>,
    ) {

        fun toEntity(userId: Long): Profile {

            return Profile(
                hookingComment = hookingComment,
                birthday = birthday,
                gender = gender,
                height = height,
                weight = weight,
                email = email,
                sns = sns,
                specialty = specialty,
                details = details,
                career = career,
                interests = interests.map { it.toString() }.toList(),
                type = type,
                domains = domains.map { it.toString() }.toList(),
                userId = userId,
                viewCount = 0,
            )
        }
    }

    data class RegisterProfileResponse(
        val profile: ProfileDto,
    ) {

        constructor(
            profile: Profile,
            userProfileWantMap: Map<Long, ProfileWant?>,
        ) : this(
            profile = ProfileDto(profile, userProfileWantMap)
        )
    }
}