package com.fone.filmone.domain.competition.service

import com.fone.filmone.common.exception.NotFoundCompetitionException
import com.fone.filmone.infrastructure.competition.CompetitionPrizeRepository
import com.fone.filmone.infrastructure.competition.CompetitionRepository
import com.fone.filmone.presentation.competition.CompetitionDto
import com.fone.filmone.presentation.competition.RetrieveCompetitionDto.RetrieveCompetitionResponse
import com.fone.filmone.presentation.competition.RetrieveCompetitionDto.RetrieveCompetitionsResponse
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.kotlin.core.publisher.toMono

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
        val competitionsFlow = competitionRepository.findBy(pageable)

        val prizes = competitionsFlow
            .map {
                it.toMono().awaitSingle()

                competitionPrizeRepository.findByCompetitionId(it.id!!)
            }.toList()

        return RetrieveCompetitionsResponse(
            pageable,
            competitionsFlow.toList().zip(prizes) { c, p -> CompetitionDto(c, p) }.toList(),
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