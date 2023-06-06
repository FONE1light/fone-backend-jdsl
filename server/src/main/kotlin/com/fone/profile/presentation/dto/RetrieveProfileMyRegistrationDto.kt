package com.fone.profile.presentation.dto

import com.fone.common.entity.CategoryType
import com.fone.common.entity.DomainType
import com.fone.profile.domain.entity.Profile
import com.fone.profile.domain.entity.ProfileWant
import com.fone.profile.presentation.dto.common.ProfileDto
import org.springframework.data.domain.Page

class RetrieveProfileMyRegistrationDto {

    data class RetrieveProfileMyRegistrationResponse(
        val profiles: Page<ProfileDto>,
    ) {

        constructor(
            profiles: Page<Profile>,
            userProfileWantMap: Map<Long, ProfileWant?>,
            profileDomains: Map<Long, List<DomainType>>,
            profileCategories: Map<Long, List<CategoryType>>,
        ) : this(
            profiles = profiles.map {
                ProfileDto(
                    it,
                    userProfileWantMap,
                    it.profileImages.map { image -> image.profileUrl }.toList(),
                    profileDomains[it.id!!] ?: listOf(),
                    profileCategories[it.id!!] ?: listOf()
                )
            }
        )
    }
}
