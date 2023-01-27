package com.fone.filmone.presentation.profile

import com.fone.filmone.domain.profile.entity.Profile
import com.fone.filmone.domain.profile.entity.ProfileWant
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

class RetrieveProfileWantDto {

    data class RetrieveProfileWantResponse(
        val profiles: Slice<ProfileDto>,
    ) {

        constructor(
            profiles: List<Profile>,
            userProfileWantMap: Map<Long, ProfileWant?>,
            pageable: Pageable,
        ) : this(
            profiles = PageImpl(
                profiles.map { ProfileDto(it, userProfileWantMap) }.toList(),
                pageable,
                profiles.size.toLong()
            )
        )
    }
}