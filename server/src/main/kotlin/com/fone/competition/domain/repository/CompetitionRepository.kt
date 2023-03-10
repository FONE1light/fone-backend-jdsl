package com.fone.competition.domain.repository

import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

interface CompetitionRepository {
    suspend fun findAll(pageable: Pageable): Slice<com.fone.competition.domain.entity.Competition>
    suspend fun count(): Long
    suspend fun findById(competitionId: Long): com.fone.competition.domain.entity.Competition?
    suspend fun findScrapAllById(
        pageable: Pageable,
        userId: Long,
    ): Slice<com.fone.competition.domain.entity.Competition>

    suspend fun save(competition: com.fone.competition.domain.entity.Competition): com.fone.competition.domain.entity.Competition
}
