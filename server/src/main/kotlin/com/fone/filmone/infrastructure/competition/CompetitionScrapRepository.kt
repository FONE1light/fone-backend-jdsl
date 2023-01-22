package com.fone.filmone.infrastructure.competition

import com.fone.filmone.domain.competition.entity.CompetitionScrap
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface CompetitionScrapRepository : CoroutineCrudRepository<CompetitionScrap, Long> {

    suspend fun findByUserIdAndCompetitionId(userId: Long, competitionId: Long): CompetitionScrap?

    suspend fun findByUserId(userId: Long): List<CompetitionScrap>
}