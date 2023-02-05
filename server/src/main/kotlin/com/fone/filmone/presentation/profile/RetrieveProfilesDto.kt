package com.fone.filmone.presentation.profile

import com.fone.filmone.domain.common.CategoryType
import com.fone.filmone.domain.common.DomainType
import com.fone.filmone.domain.profile.entity.Profile
import com.fone.filmone.domain.profile.entity.ProfileWant
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

class RetrieveProfilesDto {

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