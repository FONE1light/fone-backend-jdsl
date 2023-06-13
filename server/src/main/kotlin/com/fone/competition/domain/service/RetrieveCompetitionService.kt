package com.fone.competition.domain.service

import com.fone.common.exception.NotFoundCompetitionException
import com.fone.common.exception.NotFoundUserException
import com.fone.common.repository.UserCommonRepository
import com.fone.competition.domain.repository.CompetitionRepository
import com.fone.competition.domain.repository.CompetitionScrapRepository
import com.fone.competition.presentation.dto.RetrieveCompetitionDto.RetrieveCompetitionResponse
import com.fone.competition.presentation.dto.RetrieveCompetitionDto.RetrieveCompetitionsResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RetrieveCompetitionService(
    private val competitionRepository: CompetitionRepository,
    private val competitionScrapRepository: CompetitionScrapRepository,
    private val userRepository: UserCommonRepository,
) {

    @Transactional(readOnly = true)
    suspend fun retrieveCompetitions(
        email: String,
        pageable: Pageable,
    ): RetrieveCompetitionsResponse {
        val userId = userRepository.findByEmail(email) ?: throw NotFoundUserException()

        return coroutineScope {
            val competitions = async { competitionRepository.findAll(pageable) }

            val userCompetitionScraps = async { competitionScrapRepository.findByUserId(userId) }

            val competitionCount = async { competitionRepository.count() }

            RetrieveCompetitionsResponse(
                competitions.await(),
                userCompetitionScraps.await(),
                competitionCount.await()
            )
        }
    }

    @Transactional
    suspend fun retrieveCompetition(
        email: String,
        competitionId: Long,
    ): RetrieveCompetitionResponse {
        val userId = userRepository.findByEmail(email) ?: throw NotFoundUserException()

        return coroutineScope {
            val competition = async {
                val competition = competitionRepository.findById(competitionId) ?: throw NotFoundCompetitionException()
                competition.view()
                competitionRepository.save(competition)
            }

            val userCompetitionScraps = async { competitionScrapRepository.findByUserId(userId) }

            RetrieveCompetitionResponse(
                competition.await(),
                userCompetitionScraps.await()
            )
        }
    }
}
