package com.fone.filmone.domain.competition.repository

import com.fone.filmone.domain.competition.entity.Prize

interface CompetitionPrizeRepository {

    suspend fun saveAll(prizes: List<Prize>): List<Prize>
}