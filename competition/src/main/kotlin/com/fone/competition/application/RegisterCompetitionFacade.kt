package com.fone.competition.application

import com.fone.competition.domain.service.RegisterCompetitionService
import com.fone.competition.presentation.dto.RegisterCompetitionDto.RegisterCompetitionRequest
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
