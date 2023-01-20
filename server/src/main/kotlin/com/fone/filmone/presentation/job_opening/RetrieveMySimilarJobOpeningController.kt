package com.fone.filmone.presentation.job_opening

import com.fone.filmone.application.job_opening.RetrieveMySimilarJobOpeningFacade
import com.fone.filmone.common.response.CommonResponse
import com.fone.filmone.presentation.job_opening.RetrieveMySimilarJobOpeningDto.RetrieveMySimilarJobOpeningResponse
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.security.Principal

@RestController
@RequestMapping("/api/v1/job-openings/my-similar")
class RetrieveMySimilarJobOpeningController(
    private val retrieveMySimilarJobOpeningFacade: RetrieveMySimilarJobOpeningFacade,
) {

    @GetMapping
    suspend fun retrieveMySimilarJobOpening(principal: Principal):
            CommonResponse<RetrieveMySimilarJobOpeningResponse> {
        val response = retrieveMySimilarJobOpeningFacade.retrieveMySimilarJobOpening(principal.name)

        return CommonResponse.success(response)
    }
}