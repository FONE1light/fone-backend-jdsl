package com.fone.filmone.presentation.profile

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
            pageable: Pageable,
        ) : this(
            profiles = PageImpl(
                profiles.map {
                    ProfileDto(
                        it,
                        userProfileWantMap,
                        it.profileImages.map { image -> image.profileUrl }.toList()
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
        constructor(profile: Profile, userProfileWantMap: Map<Long, ProfileWant?>) : this(
            profile = ProfileDto(
                profile,
                userProfileWantMap,
                profile.profileImages.map { it.profileUrl }.toList()
            )
        )
    }
}