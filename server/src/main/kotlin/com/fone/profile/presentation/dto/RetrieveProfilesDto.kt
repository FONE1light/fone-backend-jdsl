package com.fone.profile.presentation.dto

import com.fone.common.entity.CategoryType
import com.fone.common.entity.DomainType
import com.fone.common.entity.Gender
import com.fone.common.entity.Type
import com.fone.profile.domain.entity.Profile
import com.fone.profile.domain.entity.ProfileWant
import com.fone.profile.presentation.dto.common.ProfileDto
import com.fone.user.domain.entity.User
import com.fone.user.domain.enum.Job
import io.swagger.annotations.ApiModelProperty
import org.springframework.data.domain.Page

data class RetrieveProfilesRequest(
    @ApiModelProperty(value = "타입", example = "ACTOR", required = true) val type: Type,
    @ApiModelProperty(value = "성별", required = false) val genders: List<Gender> = emptyList(),
    @ApiModelProperty(value = "최대 나이", required = false, example = "200") val ageMax: Int = 200,
    @ApiModelProperty(value = "최소 나이", required = false, example = "0") val ageMin: Int = 0,
    @ApiModelProperty(
        value = "카테고리",
        required = false
    ) val categories: List<CategoryType> = emptyList(),
    @ApiModelProperty(value = "분야", required = false) val domains: List<DomainType> = emptyList(),
)

data class RetrieveProfilesResponse(
    val profiles: Page<ProfileDto>,
) {
    constructor(
        profiles: Page<Profile>,
        userProfileWantMap: Map<Long, ProfileWant?>,
        profileDomains: Map<Long, List<DomainType>>,
        profileCategories: Map<Long, List<CategoryType>>,
        profileUsers: Map<Long?, User>,
    ) : this(
        profiles = profiles.map {
            ProfileDto(
                it,
                userProfileWantMap,
                it.profileImages,
                profileDomains[it.id!!] ?: listOf(),
                profileCategories[it.id!!] ?: listOf(),
                profileUsers[it.userId]?.nickname ?: "",
                profileUsers[it.userId]?.profileUrl ?: "",
                profileUsers[it.userId]?.job ?: Job.ACTOR
            )
        }
    )
}

data class RetrieveProfileResponse(
    val profile: ProfileDto,
) {
    constructor(
        profile: Profile,
        userProfileWantMap: Map<Long, ProfileWant?>,
        profileDomains: List<DomainType>,
        profileCategories: List<CategoryType>,
        nickname: String,
        profileUrl: String,
        job: Job,
    ) : this(
        profile = ProfileDto(
            profile,
            userProfileWantMap,
            profile.profileImages,
            profileDomains,
            profileCategories,
            nickname,
            profileUrl,
            job
        )
    )
}
