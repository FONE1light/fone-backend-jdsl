package com.fone.competition.domain.repository

interface CompetitionPrizeRepository {

    suspend fun saveAll(prizes: List<com.fone.competition.domain.entity.Prize>): List<com.fone.competition.domain.entity.Prize>
}
