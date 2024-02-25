package com.fone.competition.domain.service

import com.fone.common.exception.NotFoundUserException
import com.fone.common.repository.UserCommonRepository
import com.fone.competition.domain.repository.CompetitionRepository
import com.fone.competition.domain.repository.CompetitionScrapRepository
import com.fone.competition.presentation.dto.RetrieveCompetitionScrapResponse
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RetrieveCompetitionScrapService(
    private val competitionRepository: CompetitionRepository,
    private val competitionScrapRepository: CompetitionScrapRepository,
    private val userRepository: UserCommonRepository,
) {

    @Transactional(readOnly = true)
    suspend fun retrieveCompetitionScraps(
        pageable: Pageable,
        email: String,
    ): RetrieveCompetitionScrapResponse {
        val userId = userRepository.findByEmail(email) ?: throw NotFoundUserException()
        val competitions = competitionRepository.findScrapAllById(pageable, userId)
        val userCompetitionScraps = competitionScrapRepository.findByUserId(userId)
        return RetrieveCompetitionScrapResponse(
            competitions,
            userCompetitionScraps
        )
    }
}
