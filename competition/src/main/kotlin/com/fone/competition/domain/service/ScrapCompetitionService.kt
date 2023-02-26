package com.fone.competition.domain.service

import com.fone.common.exception.NotFoundCompetitionException
import com.fone.common.exception.NotFoundUserException
import com.fone.common.repository.UserCommonRepository
import com.fone.competition.domain.entity.CompetitionScrap
import com.fone.competition.domain.repository.CompetitionRepository
import com.fone.competition.domain.repository.CompetitionScrapRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ScrapCompetitionService(
    private val userRepository: UserCommonRepository,
    private val competitionRepository: CompetitionRepository,
    private val competitionScrapRepository: CompetitionScrapRepository,
) {

    @Transactional
    suspend fun scrapCompetition(email: String, competitionId: Long) {
        val userId = userRepository.findByEmail(email) ?: throw NotFoundUserException()

        competitionScrapRepository.findByUserIdAndCompetitionId(userId, competitionId)?.let {
            competitionScrapRepository.delete(it)

            val competition =
                competitionRepository.findById(competitionId)
                    ?: throw NotFoundCompetitionException()

            competition.scrapCount -= 1

            competitionRepository.save(competition)
            return
        }

        val competition =
            competitionRepository.findById(competitionId) ?: throw NotFoundCompetitionException()

        competition.scrapCount += 1

        competitionScrapRepository.save(CompetitionScrap(userId, competition))
        competitionRepository.save(competition)
    }
}
