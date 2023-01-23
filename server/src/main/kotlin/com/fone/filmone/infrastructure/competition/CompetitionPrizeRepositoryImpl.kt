package com.fone.filmone.infrastructure.competition

import com.fone.filmone.domain.competition.entity.CompetitionPrize
import com.fone.filmone.domain.competition.repository.CompetitionPrizeRepository
import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.spring.data.reactive.query.*
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.hibernate.reactive.mutiny.Mutiny.SessionFactory
import org.springframework.stereotype.Repository

@Repository
class CompetitionPrizeRepositoryImpl(
    private val sessionFactory: SessionFactory,
    private val queryFactory: SpringDataHibernateMutinyReactiveQueryFactory,
) : CompetitionPrizeRepository {

    override suspend fun findByCompetitionId(competitionId: Long): List<CompetitionPrize> {
        return queryFactory.listQuery {
            select(entity(CompetitionPrize::class))
            from(entity(CompetitionPrize::class))
            where(col(CompetitionPrize::competitionId).equal(competitionId))
        }
    }

    override suspend fun saveAll(prizes: List<CompetitionPrize>): List<CompetitionPrize> {
        sessionFactory.withSession {session ->
            session.persistAll(*prizes.toTypedArray()).flatMap { session.flush() }
        }.awaitSuspending()

        return prizes
    }
}