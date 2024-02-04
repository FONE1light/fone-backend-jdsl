package com.fone.profile.presentation.dto

import com.fone.common.entity.CategoryType
import com.fone.common.entity.DomainType
import com.fone.common.entity.Type
import com.fone.jobOpening.presentation.dto.ValidateJobOpeningDto
import com.fone.profile.domain.entity.Profile
import com.fone.profile.domain.entity.ProfileWant
import com.fone.profile.presentation.dto.common.ProfileDto
import com.fone.profile.presentation.dto.common.ProfileSnsUrl
import com.fone.user.domain.enum.Job
import io.swagger.v3.oas.annotations.media.Schema

class RegisterProfileDto {
    data class RegisterProfileRequest(
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

        @field:Schema(description = "타입", required = true)
        val type: Type,
    ) {
        fun toEntity(userId: Long): Profile {
            return Profile(
                contactMethod = firstPage.contactMethod,
                contact = firstPage.contact,
                name = secondPage.name,
                hookingComment = secondPage.hookingComment,
                representativeImageUrl = secondPage.representativeImageUrl,
                birthday = thirdPage.birthday,
                gender = thirdPage.gender,
                height = thirdPage.height,
                weight = thirdPage.weight,
                email = thirdPage.email,
                specialty = thirdPage.specialty,
                snsUrls = thirdPage.snsUrls.map(ProfileSnsUrl::toEntity).toSet(),
                details = fourthPage.details,
                career = fifthPage.career,
                careerDetail = fifthPage.careerDetail,
                type = type,
                userId = userId
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
