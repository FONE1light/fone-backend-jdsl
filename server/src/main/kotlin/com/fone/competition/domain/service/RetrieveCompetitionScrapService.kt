package com.fone.competition.domain.service

import com.fone.common.exception.NotFoundUserException
import com.fone.common.repository.UserCommonRepository
import com.fone.competition.presentation.dto.RetrieveCompetitionScrapDto.RetrieveCompetitionScrapResponse
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RetrieveCompetitionScrapService(
    private val competitionRepository: com.fone.competition.domain.repository.CompetitionRepository,
    private val competitionScrapRepository: com.fone.competition.domain.repository.CompetitionScrapRepository,
    private val userRepository: UserCommonRepository,
) {

    @Transactional(readOnly = true)
    suspend fun retrieveCompetitionScraps(
        pageable: Pageable,
        email: String,
    ): RetrieveCompetitionScrapResponse {
        val userId = userRepository.findByEmail(email) ?: throw NotFoundUserException()

        return coroutineScope {
            val competitions = async {
                competitionRepository.findScrapAllById(pageable, userId).content
            }

            val userCompetitionScraps = async { competitionScrapRepository.findByUserId(userId) }

            RetrieveCompetitionScrapResponse(
                competitions.await(),
                userCompetitionScraps.await(),
                pageable
            )
        }
    }
}
