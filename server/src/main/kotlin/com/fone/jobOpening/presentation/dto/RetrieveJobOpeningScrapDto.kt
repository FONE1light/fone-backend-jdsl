package com.fone.jobOpening.presentation.dto

import com.fone.common.entity.CategoryType
import com.fone.common.entity.DomainType
import com.fone.jobOpening.domain.entity.JobOpening
import com.fone.jobOpening.domain.entity.JobOpeningScrap
import com.fone.jobOpening.presentation.dto.common.JobOpeningDto
import com.fone.user.domain.entity.User
import com.fone.user.domain.enum.Job
import org.springframework.data.domain.Page

class RetrieveJobOpeningScrapDto {

    data class RetrieveJobOpeningScrapResponse(
        val jobOpenings: Page<JobOpeningDto>,
    ) {

        constructor(
            jobOpeningPage: Page<JobOpening>,
            userJobOpeningScrapMap: Map<Long, JobOpeningScrap?>,
            jobOpeningDomains: Map<Long, List<DomainType>>,
            jobOpeningCategories: Map<Long, List<CategoryType>>,
            jobOpeningUsers: Map<Long?, User>,
        ) : this(
            jobOpenings = jobOpeningPage.map {
                JobOpeningDto(
                    it,
                    userJobOpeningScrapMap,
                    jobOpeningDomains[it.id!!] ?: listOf(),
                    jobOpeningCategories[it.id!!] ?: listOf(),
                    jobOpeningUsers[it.userId]?.nickname ?: "",
                    jobOpeningUsers[it.userId]?.profileUrl ?: "",
                    jobOpeningUsers[it.userId]?.job ?: Job.ACTOR
                )
            }
        )
    }
}
