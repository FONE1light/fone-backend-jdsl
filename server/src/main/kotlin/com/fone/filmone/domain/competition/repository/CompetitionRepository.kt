package com.fone.filmone.domain.competition.repository

import com.fone.filmone.domain.competition.entity.Competition
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice

interface CompetitionRepository {
    suspend fun findBy(pageable: Pageable): Slice<Competition>
    suspend fun findById(competitionId: Long): Competition?
    suspend fun save(competition: Competition): Competition
    suspend fun findAllById(competitionIds: List<Long>): List<Competition>
}