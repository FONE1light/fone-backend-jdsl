package com.fone.filmone.infrastructure.competition

import com.fone.filmone.domain.competition.entity.Competition
import com.fone.filmone.domain.competition.entity.CompetitionScrap
import com.fone.filmone.domain.competition.entity.Prize
import com.fone.filmone.domain.competition.repository.CompetitionRepository
import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.querydsl.expression.column
import com.linecorp.kotlinjdsl.querydsl.from.join
import com.linecorp.kotlinjdsl.spring.data.reactive.query.SpringDataHibernateMutinyReactiveQueryFactory
import com.linecorp.kotlinjdsl.spring.data.reactive.query.pageQuery
import com.linecorp.kotlinjdsl.spring.data.reactive.query.singleQueryOrNull
import com.linecorp.kotlinjdsl.spring.data.reactive.query.subquery
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.hibernate.reactive.mutiny.Mutiny
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
class CompetitionRepositoryImpl(
    private val sessionFactory: Mutiny.SessionFactory,
    private val queryFactory: SpringDataHibernateMutinyReactiveQueryFactory,
) : CompetitionRepository {

    @Transactional
    override suspend fun findAll(pageable: Pageable): Slice<Competition> {
        return queryFactory.pageQuery(pageable) {
            select(entity(Competition::class))
            from(entity(Competition::class))
            join(Competition::class, Prize::class, on(Competition::prizes))
        }
    }

    override suspend fun findById(competitionId: Long): Competition? {
        return queryFactory.singleQueryOrNull {
            select(entity(Competition::class))
            from(entity(Competition::class))
            join(Competition::class, Prize::class, on(Competition::prizes))
            where(col(Competition::id).equal(competitionId))
        }
    }

    @Transactional
    override suspend fun findScrapAllById(
        pageable: Pageable,
        userId: Long,
    ): Slice<Competition> {
        val competitionIds = queryFactory.subquery {
            select(column(CompetitionScrap::id))
            from(entity(CompetitionScrap::class))
            where(col(CompetitionScrap::userId).equal(userId))
        }

        return queryFactory.pageQuery(pageable) {
            select(entity(Competition::class))
            from(entity(Competition::class))
            join(Competition::class, Prize::class, on(Competition::prizes))
            where(col(Competition::id).`in`(competitionIds))
        }
    }

    override suspend fun save(competition: Competition): Competition {
        return competition.also {
            queryFactory.withFactory { session, factory ->
                if (it.id == null) {
                    session.persist(it)
                } else {
                    session.merge(it)
                }
                    .flatMap { session.flush() }
                    .awaitSuspending()
            }
        }
    }
}