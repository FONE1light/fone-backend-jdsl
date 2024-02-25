package com.fone.competition.domain.service

import com.fone.common.exception.NotFoundUserException
import com.fone.common.repository.UserCommonRepository
import com.fone.competition.domain.repository.CompetitionRepository
import com.fone.competition.presentation.dto.RegisterCompetitionRequest
import com.fone.competition.presentation.dto.RegisterCompetitionResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RegisterCompetitionService(
    private val competitionRepository: CompetitionRepository,
    private val userRepository: UserCommonRepository,
) {

    @Transactional
    suspend fun registerCompetition(
        request: RegisterCompetitionRequest,
        email: String,
    ): RegisterCompetitionResponse {
        val userId = userRepository.findByEmail(email) ?: throw NotFoundUserException()

        with(request) {
            val competition = toEntity(userId)

            competitionRepository.save(competition)

            return RegisterCompetitionResponse(competition)
        }
    }
}
