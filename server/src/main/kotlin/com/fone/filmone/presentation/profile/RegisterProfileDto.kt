package com.fone.filmone.presentation.profile

import com.fone.common.entity.*
import com.fone.filmone.domain.profile.entity.Profile
import com.fone.filmone.domain.profile.entity.ProfileWant
import io.swagger.annotations.ApiModelProperty
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import javax.validation.constraints.Email

class RegisterProfileDto {

    data class RegisterProfileRequest(
        val name: String,
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
        val categories: List<CategoryType>,
        val type: Type,
        val domains: List<DomainType>,
        val userId: Long,
        val profileUrls: List<String>,
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
                sns = sns,
                specialty = specialty,
                details = details,
                career = career,
                type = type,
                userId = userId,
                viewCount = 0,
                name = name,
                profileUrl = profileUrl,
            )
        }
    }

    data class RegisterProfileResponse(
        val profile: ProfileDto,
    ) {

        constructor(
            profile: Profile,
            userProfileWantMap: Map<Long, ProfileWant?>,
            domains: List<DomainType>,
            categories: List<CategoryType>,
        ) : this(
            profile = ProfileDto(
                profile,
                userProfileWantMap,
                profile.profileImages.map { it.profileUrl }.toList(),
                domains,
                categories,
            )
        )
    }
}