package com.fone.filmone.presentation.profile

import com.fone.filmone.domain.profile.entity.Profile

class RetrieveProfileWantDto {

    data class RetrieveProfileWantResponse(
        val profiles: List<ProfileDto>,
    ) {

        constructor(profileList: ArrayList<Profile>) : this(
            profiles = profileList.map { ProfileDto(it) }.toList()
        )
    }
}