package com.fone.filmone.domain.competition.service

import com.fone.filmone.common.exception.NotFoundUserException
import com.fone.filmone.domain.competition.repository.CompetitionRepository
import com.fone.filmone.domain.user.repository.UserRepository
import com.fone.filmone.presentation.competition.CompetitionDto
import com.fone.filmone.presentation.competition.RetrieveCompetitionScrapDto.RetrieveCompetitionScrapResponse
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RetrieveCompetitionScrapService(
    private val competitionRepository: CompetitionRepository,
    private val userRepository: UserRepository,
) {

    @Transactional(readOnly = true)
    suspend fun retrieveCompetitionScraps(
        pageable: Pageable,
        email: String,
    ): RetrieveCompetitionScrapResponse {
        val user = userRepository.findByNicknameOrEmail(null, email)
            ?: throw NotFoundUserException()
        val competitions = competitionRepository.findScrapAllById(pageable, user.id!!).content

        return RetrieveCompetitionScrapResponse(
            PageImpl(
                competitions.map { CompetitionDto(it) }.toList(),
                pageable,
                competitions.size.toLong(),
            )
        )
    }
}