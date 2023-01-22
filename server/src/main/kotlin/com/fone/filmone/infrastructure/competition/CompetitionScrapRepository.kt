package com.fone.filmone.infrastructure.competition

import com.fone.filmone.domain.competition.entity.CompetitionScrap
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface CompetitionScrapRepository : CoroutineCrudRepository<CompetitionScrap, Long> {
}