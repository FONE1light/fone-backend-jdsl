package com.fone.competition.domain.repository

import com.fone.competition.domain.entity.CompetitionScrap

interface CompetitionScrapRepository {

    suspend fun findByUserIdAndCompetitionId(userId: Long, competitionId: Long): CompetitionScrap?
    suspend fun findByUserId(userId: Long): Map<Long, CompetitionScrap?>
    suspend fun delete(competitionScrap: CompetitionScrap): Int
    suspend fun save(competitionScrap: CompetitionScrap): CompetitionScrap
}