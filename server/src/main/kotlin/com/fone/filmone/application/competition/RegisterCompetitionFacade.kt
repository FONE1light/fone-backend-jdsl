package com.fone.filmone.application.competition

import com.fone.filmone.domain.competition.service.RegisterCompetitionService
import com.fone.filmone.presentation.competition.RegisterCompetitionDto.RegisterCompetitionRequest
import org.springframework.stereotype.Service

@Service
class RegisterCompetitionFacade(
    private val registerCompetitionService: RegisterCompetitionService,
) {

    suspend fun registerCompetition(
        request: RegisterCompetitionRequest,
        email: String,
    ) = registerCompetitionService.registerCompetition(request, email)
}