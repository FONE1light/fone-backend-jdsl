package com.fone.filmone.domain.competition.service

import com.fone.common.exception.NotFoundCompetitionException
import com.fone.common.exception.NotFoundUserException
import com.fone.filmone.domain.competition.repository.CompetitionRepository
import com.fone.filmone.domain.competition.repository.CompetitionScrapRepository
import com.fone.filmone.presentation.competition.RetrieveCompetitionDto.RetrieveCompetitionResponse
import com.fone.filmone.presentation.competition.RetrieveCompetitionDto.RetrieveCompetitionsResponse
import com.fone.user.domain.repository.UserRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RetrieveCompetitionService(
    private val competitionRepository: CompetitionRepository,
    private val competitionScrapRepository: CompetitionScrapRepository,
    private val userRepository: UserRepository,
) {

    @Transactional(readOnly = true)
    suspend fun retrieveCompetitions(
        email: String,
        pageable: Pageable,
    ): RetrieveCompetitionsResponse {
        val user = userRepository.findByNicknameOrEmail(null, email)
            ?: throw NotFoundUserException()

        return coroutineScope {
            val competitions = async {
                competitionRepository.findAll(pageable).content
            }

            val userCompetitionScraps = async {
                competitionScrapRepository.findByUserId(user.id!!)
            }

            val competitionCount = async {
                competitionRepository.count()
            }

            RetrieveCompetitionsResponse(
                competitions.await(),
                userCompetitionScraps.await(),
                competitionCount.await(),
                pageable,
            )
        }
    }

    @Transactional
    suspend fun retrieveCompetition(
        email: String,
        competitionId: Long,
    ): RetrieveCompetitionResponse {
        val user = userRepository.findByNicknameOrEmail(null, email)
            ?: throw NotFoundUserException()

        return coroutineScope {
            val competition = async {
                val competition = competitionRepository.findById(competitionId)
                    ?: throw NotFoundCompetitionException()
                competition.view()
                competition
            }

            val userCompetitionScraps = async {
                competitionScrapRepository.findByUserId(user.id!!)
            }

            RetrieveCompetitionResponse(
                competition.await(),
                userCompetitionScraps.await(),
            )
        }
    }
}