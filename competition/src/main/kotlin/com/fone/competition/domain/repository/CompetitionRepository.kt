package com.fone.competition.domain.repository

import com.fone.competition.domain.entity.Competition
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

interface CompetitionRepository {
    suspend fun findAll(pageable: Pageable): Slice<Competition>
    suspend fun count(): Long
    suspend fun findById(competitionId: Long): Competition?
    suspend fun findScrapAllById(
        pageable: Pageable,
        userId: Long
    ): Slice<Competition>

    suspend fun save(competition: Competition): Competition
}
