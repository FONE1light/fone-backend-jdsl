package com.fone.filmone.domain.competition.repository

import com.fone.filmone.domain.competition.entity.CompetitionPrize

interface CompetitionPrizeRepository {

    suspend fun findByCompetitionId(competitionId: Long): List<CompetitionPrize>

    suspend fun saveAll(prizes: List<CompetitionPrize>): List<CompetitionPrize>
}