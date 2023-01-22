package com.fone.filmone.infrastructure.competition

import com.fone.filmone.domain.competition.entity.Competition
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface CompetitionRepository : CoroutineCrudRepository<Competition, Long> {

}