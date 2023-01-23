package com.fone.filmone.infrastructure.competition

import com.fone.filmone.domain.competition.entity.CompetitionScrap

interface CompetitionScrapRepository {

    suspend fun findByUserIdAndCompetitionId(userId: Long, competitionId: Long): CompetitionScrap?

    suspend fun findByUserId(userId: Long): List<CompetitionScrap>
}