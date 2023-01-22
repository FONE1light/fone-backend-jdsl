package com.fone.filmone.domain.competition.service

import com.fone.filmone.infrastructure.competition.CompetitionPrizeRepository
import com.fone.filmone.infrastructure.competition.CompetitionRepository
import com.fone.filmone.presentation.competition.CompetitionDto
import com.fone.filmone.presentation.competition.RetrieveCompetitionDto.RetrieveCompetitionResponse
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
    suspend fun retrieveCompetition(
        email: String,
        pageable: Pageable,
    ): RetrieveCompetitionResponse {
        val competitionsFlow = competitionRepository.findBy(pageable)

        val prizes = competitionsFlow
            .map {
                it.toMono().awaitSingle()

                competitionPrizeRepository.findByCompetitionId(it.id!!)
            }.toList()

        return RetrieveCompetitionResponse(
            pageable,
            competitionsFlow.toList().zip(prizes) { c, p -> CompetitionDto(c, p) }.toList(),
        )
    }
}