package com.fone.filmone.domain.competition.service

import com.fone.filmone.common.exception.NotFoundUserException
import com.fone.filmone.domain.competition.entity.CompetitionScrap
import com.fone.filmone.infrastructure.competition.CompetitionPrizeRepository
import com.fone.filmone.infrastructure.competition.CompetitionRepository
import com.fone.filmone.infrastructure.competition.CompetitionScrapRepository
import com.fone.filmone.infrastructure.user.UserRepository
import com.fone.filmone.presentation.competition.RetrieveCompetitionScrapDto.RetrieveCompetitionScrapResponse
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.kotlin.core.publisher.toMono

@Service
class RetrieveCompetitionScrapService(
    private val competitionRepository: CompetitionRepository,
    private val competitionScrapRepository: CompetitionScrapRepository,
    private val competitionPrizeRepository: CompetitionPrizeRepository,
    private val userRepository: UserRepository,
) {

    @Transactional(readOnly = true)
    suspend fun retrieveCompetitionScraps(
        email: String,
    ): RetrieveCompetitionScrapResponse {
        val user = userRepository.findByEmail(email) ?: throw NotFoundUserException()
        val competitionScraps = competitionScrapRepository.findByUserId(user.id!!)
        val competitionIds = competitionScraps
            .map(CompetitionScrap::competitionId)
            .toList()

        val competitions = competitionRepository.findAllById(competitionIds)
            .map {
                it.toMono().awaitSingle()
            }.toList() as ArrayList

        val prizes = competitions
            .map {
                competitionPrizeRepository.findByCompetitionId(it.id!!)
            }.toList()

        return RetrieveCompetitionScrapResponse(competitions, prizes)
    }
}