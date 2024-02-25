@file:Suppress("ktlint")

package com.fone.profile.presentation.dto

import com.fone.common.entity.CategoryType
import com.fone.common.entity.DomainType
import com.fone.profile.domain.entity.Profile
import com.fone.profile.domain.entity.ProfileWant
import com.fone.profile.presentation.dto.common.ProfileDto
import com.fone.user.domain.entity.User
import com.fone.user.domain.enum.Job
import org.springframework.data.domain.Page

data class RetrieveProfileMyRegistrationResponse(
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
