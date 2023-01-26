package com.fone.filmone.domain.competition.repository

import com.fone.filmone.domain.competition.entity.Competition
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

interface CompetitionRepository {
    suspend fun findAll(pageable: Pageable): Slice<Competition>
    suspend fun findById(competitionId: Long): Competition?
    suspend fun findScrapAllById(
        pageable: Pageable,
        userId: Long,
    ): Slice<Competition>

    suspend fun save(competition: Competition): Competition
}