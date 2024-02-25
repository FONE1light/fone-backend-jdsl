package com.fone.competition.domain.service

import com.fone.common.exception.NotFoundCompetitionException
import com.fone.common.exception.NotFoundUserException
import com.fone.common.repository.UserCommonRepository
import com.fone.competition.domain.repository.CompetitionRepository
import com.fone.competition.domain.repository.CompetitionScrapRepository
import com.fone.competition.presentation.dto.RetrieveCompetitionResponse
import com.fone.competition.presentation.dto.RetrieveCompetitionsResponse
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

        val competitions = competitionRepository.findAll(pageable)
        val userCompetitionScraps = competitionScrapRepository.findByUserId(userId)
        val competitionCount = competitionRepository.count()

        return RetrieveCompetitionsResponse(competitions, userCompetitionScraps, competitionCount)
    }

    @Transactional
    suspend fun retrieveCompetition(
        email: String,
        competitionId: Long,
    ): RetrieveCompetitionResponse {
        val userId = userRepository.findByEmail(email) ?: throw NotFoundUserException()
        val competition = competitionRepository.findById(competitionId) ?: throw NotFoundCompetitionException()
        competition.view()
        val savedCompetition = competitionRepository.save(competition)

        val userCompetitionScraps = competitionScrapRepository.findByUserId(userId)

        return RetrieveCompetitionResponse(
            savedCompetition,
            userCompetitionScraps
        )
    }
}
