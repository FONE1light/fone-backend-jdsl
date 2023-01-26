package com.fone.filmone.domain.competition.service

import com.fone.filmone.common.exception.NotFoundUserException
import com.fone.filmone.domain.competition.repository.CompetitionPrizeRepository
import com.fone.filmone.domain.competition.repository.CompetitionRepository
import com.fone.filmone.domain.user.repository.UserRepository
import com.fone.filmone.presentation.competition.CompetitionDto
import com.fone.filmone.presentation.competition.RegisterCompetitionDto.RegisterCompetitionRequest
import com.fone.filmone.presentation.competition.RegisterCompetitionDto.RegisterCompetitionResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RegisterCompetitionService(
    private val competitionRepository: CompetitionRepository,
    private val userRepository: UserRepository,
) {

    @Transactional
    suspend fun registerCompetition(
        request: RegisterCompetitionRequest,
        email: String,
    ): RegisterCompetitionResponse {
        val user = userRepository.findByNicknameOrEmail(null, email)
            ?: throw NotFoundUserException()

        with(request) {
            val competition = toEntity(user.id!!)
            prizes.forEach { competition.addPrize(it.toEntity()) }

            competitionRepository.save(competition)

            return RegisterCompetitionResponse(CompetitionDto(competition))
        }
    }
}