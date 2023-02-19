package com.fone.filmone.domain.competition.service

import com.fone.common.exception.NotFoundCompetitionException
import com.fone.common.exception.NotFoundUserException
import com.fone.filmone.domain.competition.entity.CompetitionScrap
import com.fone.filmone.domain.competition.repository.CompetitionRepository
import com.fone.filmone.domain.competition.repository.CompetitionScrapRepository
import com.fone.user.domain.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ScrapCompetitionService(
    private val userRepository: UserRepository,
    private val competitionRepository: CompetitionRepository,
    private val competitionScrapRepository: CompetitionScrapRepository,
) {

    @Transactional
    suspend fun scrapCompetition(email: String, competitionId: Long) {
        val user = userRepository.findByNicknameOrEmail(null, email)
            ?: throw NotFoundUserException()

        competitionScrapRepository.findByUserIdAndCompetitionId(user.id!!, competitionId)
            ?.let {
                competitionScrapRepository.delete(it)

                val competition = competitionRepository.findById(competitionId)
                    ?: throw NotFoundCompetitionException()

                competition.scrapCount -= 1

                competitionRepository.save(competition)
                return
            }

        val competition = competitionRepository.findById(competitionId)
            ?: throw NotFoundCompetitionException()

        competition.scrapCount += 1

        competitionScrapRepository.save(CompetitionScrap(user.id!!, competition))
        competitionRepository.save(competition)
    }
}