package com.fone.filmone.domain.competition.repository

import com.fone.filmone.domain.competition.entity.CompetitionPrize

interface CompetitionPrizeRepository {

    suspend fun findByCompetitionId(competitionId: Long): List<CompetitionPrize>
}