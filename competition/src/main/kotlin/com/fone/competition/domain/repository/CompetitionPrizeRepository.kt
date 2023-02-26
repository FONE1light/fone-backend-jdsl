package com.fone.competition.domain.repository

import com.fone.competition.domain.entity.Prize

interface CompetitionPrizeRepository {

    suspend fun saveAll(prizes: List<Prize>): List<Prize>
}
