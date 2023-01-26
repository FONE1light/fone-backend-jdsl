package com.fone.filmone.domain.competition.service

import com.fone.filmone.common.exception.NotFoundCompetitionException
import com.fone.filmone.domain.competition.repository.CompetitionPrizeRepository
import com.fone.filmone.domain.competition.repository.CompetitionRepository
import com.fone.filmone.presentation.competition.CompetitionDto
import com.fone.filmone.presentation.competition.RetrieveCompetitionDto.RetrieveCompetitionResponse
import com.fone.filmone.presentation.competition.RetrieveCompetitionDto.RetrieveCompetitionsResponse
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RetrieveCompetitionService(
    private val competitionRepository: CompetitionRepository,
) {

    @Transactional(readOnly = true)
    suspend fun retrieveCompetitions(
        email: String,
        pageable: Pageable,
    ): RetrieveCompetitionsResponse {
        val competitions = competitionRepository.findAll(pageable)

        return RetrieveCompetitionsResponse(
            pageable,
            competitions.map { CompetitionDto(it) }.toList(),
        )
    }

    @Transactional
    suspend fun retrieveCompetition(
        email: String,
        competitionId: Long,
    ): RetrieveCompetitionResponse {
        val competition = competitionRepository.findById(competitionId)
            ?: throw NotFoundCompetitionException()

        competition.view()

        return RetrieveCompetitionResponse(competition)
    }
}