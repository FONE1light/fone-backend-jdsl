package com.fone.competition.domain.repository

import com.fone.competition.domain.entity.Competition
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface CompetitionRepository {
    suspend fun findAll(pageable: Pageable): Page<Competition>
    suspend fun count(): Long
    suspend fun findById(competitionId: Long): Competition?
    suspend fun findScrapAllById(
        pageable: Pageable,
        userId: Long,
    ): Page<Competition>

    suspend fun save(competition: Competition): Competition
}
