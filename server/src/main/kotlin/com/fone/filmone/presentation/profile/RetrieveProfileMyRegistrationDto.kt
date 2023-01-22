package com.fone.filmone.presentation.profile

import com.fone.filmone.domain.profile.entity.Profile

class RetrieveProfileMyRegistrationDto {

    data class RetrieveProfileMyRegistrationResponse(
        val profiles: List<ProfileDto>,
    ) {

        constructor(profileList: ArrayList<Profile>) : this(
            profiles = profileList.map { ProfileDto(it) }.toList()
        )
    }
}