package com.fone.filmone.presentation.profile

import com.fone.filmone.domain.common.CategoryType
import com.fone.filmone.domain.common.DomainType
import com.fone.filmone.domain.common.Gender
import com.fone.filmone.domain.common.Type
import com.fone.filmone.domain.profile.entity.Profile
import com.fone.filmone.domain.profile.entity.ProfileWant
import io.swagger.annotations.ApiModelProperty
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

class RetrieveProfilesDto {

    data class RetrieveProfilesRequest(
        @ApiModelProperty(value = "타입", example = "ACTOR", required = true)
        val type: Type,
        @ApiModelProperty(value = "성별", required = false)
        val genders: List<Gender> = Gender.getAllEnum(),
        @ApiModelProperty(value = "최대 나이", required = false)
        val ageMax: Int = 200,
        @ApiModelProperty(value = "최소 나이", required = false)
        val ageMin: Int = 0,
        @ApiModelProperty(value = "카테고리", required = false)
        val categories: List<CategoryType> = CategoryType.getAllEnum(),
    )

    data class RetrieveProfilesResponse(
        val profiles: Slice<ProfileDto>,
    ) {
        constructor(
            profiles: List<Profile>,
            userProfileWantMap: Map<Long, ProfileWant?>,
            profileDomains: Map<Long, List<DomainType>>,
            profileCategories: Map<Long, List<CategoryType>>,
            pageable: Pageable,
        ) : this(
            profiles = PageImpl(
                profiles.map {
                    ProfileDto(
                        it,
                        userProfileWantMap,
                        it.profileImages.map { image -> image.profileUrl }.toList(),
                        profileDomains[it.id!!] ?: listOf(),
                        profileCategories[it.id!!] ?: listOf(),
                    )
                }.toList(),
                pageable,
                profiles.size.toLong()
            )
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
        ) : this(
            profile = ProfileDto(
                profile,
                userProfileWantMap,
                profile.profileImages.map { it.profileUrl }.toList(),
                profileDomains,
                profileCategories,
            )
        )
    }
}