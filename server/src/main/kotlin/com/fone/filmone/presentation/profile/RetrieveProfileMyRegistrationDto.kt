package com.fone.filmone.presentation.profile

import com.fone.common.entity.CategoryType
import com.fone.common.entity.DomainType
import com.fone.filmone.domain.profile.entity.Profile
import com.fone.filmone.domain.profile.entity.ProfileWant
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

class RetrieveProfileMyRegistrationDto {

    data class RetrieveProfileMyRegistrationResponse(
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
}