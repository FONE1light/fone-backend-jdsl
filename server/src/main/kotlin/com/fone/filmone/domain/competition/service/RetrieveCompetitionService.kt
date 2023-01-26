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
    private val competitionPrizeRepository: CompetitionPrizeRepository,
) {

    @Transactional(readOnly = true)
    suspend fun retrieveCompetitions(
        email: String,
        pageable: Pageable,
    ): RetrieveCompetitionsResponse {
        val competitions = competitionRepository.findAll(pageable)

        val prizes = competitions.content
            .map {
                competitionPrizeRepository.findByCompetitionId(it.id!!)
            }.toList()

        return RetrieveCompetitionsResponse(
            pageable,
            competitions.zip(prizes) { c, p -> CompetitionDto(c, p) }.toList(),
        )
    }

    @Transactional
    suspend fun retrieveCompetition(
        email: String,
        competitionId: Long,
    ): RetrieveCompetitionResponse {
        val competition = competitionRepository.findById(competitionId)
            ?: throw NotFoundCompetitionException()
        val prizes = competitionPrizeRepository.findByCompetitionId(competition.id!!).toList()

        competition.view()

        return RetrieveCompetitionResponse(competition, prizes)
    }
}