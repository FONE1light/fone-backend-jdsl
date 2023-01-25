package com.fone.filmone.presentation.profile

import com.fone.filmone.domain.profile.entity.Profile
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

class RetrieveProfileMyRegistrationDto {

    data class RetrieveProfileMyRegistrationResponse(
        val profiles: Slice<ProfileDto>,
    ) {

        constructor(profileList: List<Profile>, pageable: Pageable) : this(
            profiles = PageImpl(
                profileList.map { ProfileDto(it) }.toList(),
                pageable,
                profileList.size.toLong()
            )
        )
    }
}