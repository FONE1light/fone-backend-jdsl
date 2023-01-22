package com.fone.filmone.domain.competition.service

import com.fone.filmone.common.exception.NotFoundUserException
import com.fone.filmone.domain.competition.entity.CompetitionScrap
import com.fone.filmone.infrastructure.competition.CompetitionScrapRepository
import com.fone.filmone.infrastructure.user.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ScrapCompetitionService(
    private val userRepository: UserRepository,
    private val competitionScrapRepository: CompetitionScrapRepository,
) {

    @Transactional
    suspend fun scrapCompetition(email: String, competitionId: Long) {
        val user = userRepository.findByEmail(email) ?: throw NotFoundUserException()

        competitionScrapRepository.findByUserIdAndCompetitionId(user.id!!, competitionId)
            ?.let {
                competitionScrapRepository.delete(it)
                return
            }

        val competitionScrap = CompetitionScrap(user.id!!, competitionId)
        competitionScrapRepository.save(competitionScrap)
    }
}