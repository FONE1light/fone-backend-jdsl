package com.fone.filmone.infrastructure.competition

import com.fone.filmone.domain.competition.entity.CompetitionPrize

interface CompetitionPrizeRepository {

    suspend fun findByCompetitionId(competitionId: Long): List<CompetitionPrize>
}