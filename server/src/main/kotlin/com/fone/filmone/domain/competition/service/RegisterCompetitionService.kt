package com.fone.filmone.domain.competition.service

import com.fone.filmone.common.exception.NotFoundUserException
import com.fone.filmone.infrastructure.competition.CompetitionPrizeRepository
import com.fone.filmone.infrastructure.competition.CompetitionRepository
import com.fone.filmone.infrastructure.user.UserRepository
import com.fone.filmone.presentation.competition.RegisterCompetitionDto.RegisterCompetitionRequest
import com.fone.filmone.presentation.competition.RegisterCompetitionDto.RegisterCompetitionResponse
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.kotlin.core.publisher.toMono

@Service
class RegisterCompetitionService(
    private val competitionRepository: CompetitionRepository,
    private val competitionPrizeRepository: CompetitionPrizeRepository,
    private val userRepository: UserRepository,
) {

    @Transactional
    suspend fun registerCompetition(
        request: RegisterCompetitionRequest,
        email: String,
    ): RegisterCompetitionResponse {
        val user = userRepository.findByEmail(email) ?: throw NotFoundUserException()

        with(request) {
            val competition = toEntity(user.id!!)
            competitionRepository.save(competition)

            val prizes = this.prizes.map { it.toEntity(competition.id!!) }.toList()
            competitionPrizeRepository.saveAll(prizes).collect { it.toMono().awaitSingle() }

            return RegisterCompetitionResponse(competition, prizes)
        }
    }
}