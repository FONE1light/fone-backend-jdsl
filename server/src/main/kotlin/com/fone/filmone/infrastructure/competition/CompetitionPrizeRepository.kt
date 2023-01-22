package com.fone.filmone.infrastructure.competition

import com.fone.filmone.domain.competition.entity.CompetitionPrize
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface CompetitionPrizeRepository : CoroutineCrudRepository<CompetitionPrize, Long> {
}