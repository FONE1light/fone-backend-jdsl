package com.fone.filmone.infrastructure.competition

import com.fone.filmone.domain.competition.entity.Competition
import com.fone.filmone.domain.competition.entity.CompetitionScrap
import com.fone.filmone.domain.competition.entity.Prize
import com.fone.filmone.domain.competition.repository.CompetitionRepository
import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.querydsl.expression.column
import com.linecorp.kotlinjdsl.querydsl.from.fetch
import com.linecorp.kotlinjdsl.spring.data.reactive.query.*
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.hibernate.reactive.mutiny.Mutiny
import org.springframework.data.domain.PageImpl
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
        val ids = queryFactory.pageQuery(pageable) {
            select(column(Competition::id))
            from(entity(Competition::class))
        }.content

        val competitions = queryFactory.listQuery {
            select(entity(Competition::class))
            from(entity(Competition::class))
            fetch(Competition::class, Prize::class, on(Competition::prizes))
            where(col(Competition::id).`in`(ids))
        }

        return PageImpl(
            competitions,
            pageable,
            competitions.size.toLong()
        )
    }

    override suspend fun findById(competitionId: Long): Competition? {
        return queryFactory.singleQueryOrNull {
            select(entity(Competition::class))
            from(entity(Competition::class))
            fetch(Competition::prizes)
            where(col(Competition::id).equal(competitionId))
        }
    }

    @Transactional
    override suspend fun findScrapAllById(
        pageable: Pageable,
        userId: Long,
    ): Slice<Competition> {
        val ids = queryFactory.pageQuery(pageable) {
            select(column(CompetitionScrap::id))
            from(entity(CompetitionScrap::class))
            where(col(CompetitionScrap::userId).equal(userId))
        }.content

        val competitions = queryFactory.listQuery {
            select(entity(Competition::class))
            from(entity(Competition::class))
            fetch(Competition::class, Prize::class, on(Competition::prizes))
            where(col(Competition::id).`in`(ids))
        }

        return PageImpl(
            competitions,
            pageable,
            competitions.size.toLong()
        )
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