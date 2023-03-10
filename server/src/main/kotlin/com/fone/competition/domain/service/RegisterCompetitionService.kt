package com.fone.competition.domain.service

import com.fone.common.exception.NotFoundUserException
import com.fone.common.repository.UserCommonRepository
import com.fone.competition.presentation.dto.RegisterCompetitionDto.RegisterCompetitionRequest
import com.fone.competition.presentation.dto.RegisterCompetitionDto.RegisterCompetitionResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RegisterCompetitionService(
    private val competitionRepository: com.fone.competition.domain.repository.CompetitionRepository,
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
            prizes.forEach { competition.addPrize(it.toEntity()) }

            competitionRepository.save(competition)

            return RegisterCompetitionResponse(competition)
        }
    }
}
