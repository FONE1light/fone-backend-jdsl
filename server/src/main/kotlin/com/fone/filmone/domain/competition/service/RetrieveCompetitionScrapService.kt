package com.fone.filmone.domain.competition.service

import com.fone.filmone.common.exception.NotFoundUserException
import com.fone.filmone.domain.competition.entity.CompetitionScrap
import com.fone.filmone.domain.competition.repository.CompetitionPrizeRepository
import com.fone.filmone.domain.competition.repository.CompetitionRepository
import com.fone.filmone.domain.competition.repository.CompetitionScrapRepository
import com.fone.filmone.domain.user.repository.UserRepository
import com.fone.filmone.presentation.competition.RetrieveCompetitionScrapDto.RetrieveCompetitionScrapResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

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
        val user = userRepository.findByNicknameOrEmail(null, email)
            ?: throw NotFoundUserException()
        val competitionScraps = competitionScrapRepository.findByUserId(user.id!!)
        val competitionIds = competitionScraps
            .map(CompetitionScrap::competitionId)
            .toList()

        val competitions = competitionRepository.findAllById(competitionIds) as ArrayList

        val prizes = competitions
            .map {
                competitionPrizeRepository.findByCompetitionId(it.id!!)
            }.toList()

        return RetrieveCompetitionScrapResponse(competitions, prizes)
    }
}