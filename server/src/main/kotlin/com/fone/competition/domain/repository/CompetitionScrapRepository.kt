package com.fone.competition.domain.repository

interface CompetitionScrapRepository {

    suspend fun findByUserIdAndCompetitionId(
        userId: Long,
        competitionId: Long,
    ): com.fone.competition.domain.entity.CompetitionScrap?

    suspend fun findByUserId(userId: Long): Map<Long, com.fone.competition.domain.entity.CompetitionScrap?>
    suspend fun delete(competitionScrap: com.fone.competition.domain.entity.CompetitionScrap): Int
    suspend fun save(competitionScrap: com.fone.competition.domain.entity.CompetitionScrap): com.fone.competition.domain.entity.CompetitionScrap
}
