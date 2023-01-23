package com.fone.filmone.infrastructure.competition

import com.fone.filmone.domain.competition.entity.Competition
import com.fone.filmone.domain.competition.repository.CompetitionRepository
import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.spring.data.reactive.query.SpringDataHibernateMutinyReactiveQueryFactory
import com.linecorp.kotlinjdsl.spring.data.reactive.query.listQuery
import com.linecorp.kotlinjdsl.spring.data.reactive.query.pageQuery
import com.linecorp.kotlinjdsl.spring.data.reactive.query.singleQuery
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.hibernate.reactive.mutiny.Mutiny
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.stereotype.Repository

@Repository
class CompetitionRepositoryImpl(
    private val sessionFactory: Mutiny.SessionFactory,
    private val queryFactory: SpringDataHibernateMutinyReactiveQueryFactory,
) : CompetitionRepository {

    override suspend fun findBy(pageable: Pageable): Slice<Competition> {
        return queryFactory.pageQuery(pageable) {
            select(entity(Competition::class))
            from(entity(Competition::class))
        }
    }

    override suspend fun findById(competitionId: Long): Competition? {
        return queryFactory.singleQuery {
            select(entity(Competition::class))
            from(entity(Competition::class))
            where(col(Competition::id).equal(competitionId))
        }
    }

    override suspend fun save(competition: Competition): Competition {
        return competition.also {
            sessionFactory.withSession { session ->
                session.persist(it).flatMap { session.flush() }
            }.awaitSuspending()
        }
    }

    override suspend fun findAllById(competitionIds: List<Long>): List<Competition> {
        return queryFactory.listQuery {
            select(entity(Competition::class))
            from(entity(Competition::class))
            where(col(Competition::id).`in`(competitionIds))
        }
    }
}