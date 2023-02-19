package com.fone.filmone.domain.competition.service

import com.fone.common.exception.NotFoundUserException
import com.fone.filmone.domain.competition.repository.CompetitionRepository
import com.fone.filmone.domain.competition.repository.CompetitionScrapRepository
import com.fone.filmone.presentation.competition.RetrieveCompetitionScrapDto.RetrieveCompetitionScrapResponse
import com.fone.user.domain.repository.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RetrieveCompetitionScrapService(
    private val competitionRepository: CompetitionRepository,
    private val competitionScrapRepository: CompetitionScrapRepository,
    private val userRepository: UserRepository,
) {

    @Transactional(readOnly = true)
    suspend fun retrieveCompetitionScraps(
        pageable: Pageable,
        email: String,
    ): RetrieveCompetitionScrapResponse {
        val user = userRepository.findByNicknameOrEmail(null, email)
            ?: throw NotFoundUserException()

        return coroutineScope {
            val competitions = async {
                competitionRepository.findScrapAllById(pageable, user.id!!).content
            }

            val userCompetitionScraps = async {
                competitionScrapRepository.findByUserId(user.id!!)
            }

            RetrieveCompetitionScrapResponse(
                competitions.await(),
                userCompetitionScraps.await(),
                pageable,
            )
        }
    }
}