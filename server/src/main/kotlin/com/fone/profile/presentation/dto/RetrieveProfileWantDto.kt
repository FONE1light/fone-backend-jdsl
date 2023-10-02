package com.fone.profile.presentation.dto

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import com.fone.common.entity.CategoryType
import com.fone.common.entity.DomainType
import com.fone.profile.domain.entity.Profile
import com.fone.profile.domain.entity.ProfileWant
import com.fone.profile.presentation.dto.common.ProfileDto
import com.fone.user.domain.entity.User
import com.fone.user.domain.enum.Job
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable

class RetrieveProfileWantDto {

    data class RetrieveProfileWantResponse(
        val profiles: RestPage<ProfileDto>,
    ) {

        constructor(
            profiles: List<Profile>,
            userProfileWantMap: Map<Long, ProfileWant?>,
            profileDomains: Map<Long, List<DomainType>>,
            profileCategories: Map<Long, List<CategoryType>>,
            profileUsers: Map<Long?, User>,
            pageable: Pageable,
        ) : this(
            profiles = RestPage(
                profiles.map {
                    ProfileDto(
                        it,
                        userProfileWantMap,
                        it.profileImages.map { image -> image.profileUrl }.toList(),
                        profileDomains[it.id!!] ?: listOf(),
                        profileCategories[it.id!!] ?: listOf(),
                        profileUsers[it.userId]?.nickname ?: "",
                        profileUsers[it.userId]?.profileUrl ?: "",
                        profileUsers[it.userId]?.job ?: Job.ACTOR
                    )
                }.toList(),
                pageable.pageNumber,
                pageable.pageSize,
                profiles.size.toLong()
            )
        )
    }

    @JsonIgnoreProperties(ignoreUnknown = true, value = ["pageable"])
    class RestPage<T> : PageImpl<T> {
        @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
        constructor(
            @JsonProperty("content") content: List<T>?,
            @JsonProperty("number") page: Int,
            @JsonProperty("size") size: Int,
            @JsonProperty("totalElements") total: Long,
        ) : super(content!!, PageRequest.of(page, size), total)

        constructor(page: Page<T>) : super(page.content, page.pageable, page.totalElements)
    }
}
