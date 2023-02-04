package com.fone.filmone.infrastructure.competition

import com.fone.filmone.domain.competition.entity.Prize
import com.fone.filmone.domain.competition.repository.CompetitionPrizeRepository
import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.spring.data.reactive.query.SpringDataHibernateMutinyReactiveQueryFactory
import com.linecorp.kotlinjdsl.spring.data.reactive.query.listQuery
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.hibernate.reactive.mutiny.Mutiny.SessionFactory
import org.springframework.stereotype.Repository

@Repository
class CompetitionPrizeRepositoryImpl(
    private val sessionFactory: SessionFactory,
    private val queryFactory: SpringDataHibernateMutinyReactiveQueryFactory,
) : CompetitionPrizeRepository {

    override suspend fun saveAll(prizes: List<Prize>): List<Prize> {
        return prizes.also {
            sessionFactory.withSession { session ->
                session.persistAll(*it.toTypedArray()).flatMap { session.flush() }
            }.awaitSuspending()
        }
    }
}