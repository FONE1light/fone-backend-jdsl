package com.fone.filmone.domain.competition.repository

import com.fone.filmone.domain.competition.entity.Competition
import kotlinx.coroutines.flow.Flow
import org.springframework.data.domain.Pageable

interface CompetitionRepository {
    fun findBy(pageable: Pageable): Flow<Competition>
}