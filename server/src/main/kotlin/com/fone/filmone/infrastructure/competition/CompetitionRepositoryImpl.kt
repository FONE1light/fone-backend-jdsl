package com.fone.filmone.infrastructure.competition

import com.fone.filmone.domain.competition.entity.Competition
import com.fone.filmone.domain.competition.entity.CompetitionScrap
import com.fone.filmone.domain.competition.entity.Prize
import com.fone.filmone.domain.competition.repository.CompetitionRepository
import com.linecorp.kotlinjdsl.query.spec.OrderSpec
import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.querydsl.expression.column
import com.linecorp.kotlinjdsl.querydsl.from.fetch
import com.linecorp.kotlinjdsl.querydsl.from.join
import com.linecorp.kotlinjdsl.spring.data.reactive.query.SpringDataHibernateMutinyReactiveQueryFactory
import com.linecorp.kotlinjdsl.spring.data.reactive.query.listQuery
import com.linecorp.kotlinjdsl.spring.data.reactive.query.pageQuery
import com.linecorp.kotlinjdsl.spring.data.reactive.query.singleQuery
import com.linecorp.kotlinjdsl.spring.reactive.querydsl.SpringDataReactiveCriteriaQueryDsl
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.hibernate.reactive.mutiny.Mutiny
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class CompetitionRepositoryImpl(
    private val sessionFactory: Mutiny.SessionFactory,
    private val queryFactory: SpringDataHibernateMutinyReactiveQueryFactory,
) : CompetitionRepository {

    override suspend fun findAll(pageable: Pageable): Slice<Competition> {
        val ids = queryFactory.pageQuery(pageable) {
            select(column(Competition::id))
            from(entity(Competition::class))
        }.content

        val competitions = queryFactory.listQuery {
            select(entity(Competition::class))
            from(entity(Competition::class))
            fetch(Competition::class, Prize::class, on(Competition::prizes))
            where(
                and(
                    col(Competition::id).`in`(ids),
                    col(Competition::showStartDate).lessThanOrEqualTo(LocalDate.now())
                )
            )
            orderBy(
                orderSpec(pageable.sort),
            )
        }

        return PageImpl(
            competitions, pageable, competitions.size.toLong()
        )
    }

    override suspend fun count(): Long {
        return queryFactory.singleQuery {
            select(count(column(Competition::id)))
            from(entity(Competition::class))
        }
    }

    override suspend fun findById(competitionId: Long): Competition? {
        return queryFactory.singleQuery {
            select(entity(Competition::class))
            from(entity(Competition::class))
            fetch(Competition::prizes)
            where(col(Competition::id).equal(competitionId))
        }
    }

    override suspend fun findScrapAllById(
        pageable: Pageable,
        userId: Long,
    ): Slice<Competition> {
        val ids = queryFactory.pageQuery(pageable) {
            select(column(Competition::id))
            from(entity(CompetitionScrap::class))
            join(CompetitionScrap::competition)
            where(col(CompetitionScrap::userId).equal(userId))
        }.content

        val competitions = queryFactory.listQuery {
            select(entity(Competition::class))
            from(entity(Competition::class))
            fetch(Competition::prizes)
            where(col(Competition::id).`in`(ids))
        }

        return PageImpl(
            competitions, pageable, competitions.size.toLong()
        )
    }

    override suspend fun save(competition: Competition): Competition {
        return competition.also {
            queryFactory.withFactory { session, factory ->
                if (it.id == null) {
                    session.persist(it)
                } else {
                    session.merge(it)
                }.flatMap { session.flush() }.awaitSuspending()
            }
        }
    }

    private fun SpringDataReactiveCriteriaQueryDsl<Competition?>.orderSpec(sort: Sort): List<OrderSpec> {
        val endDate = case(
            `when`(column(Competition::submitEndDate).lessThanOrEqualTo(LocalDate.now())).then(
                literal(1)
            ),
            `when`(column(Competition::submitEndDate).isNull()).then(
                case(
                    `when`(column(Competition::endDate).lessThanOrEqualTo(LocalDate.now())).then(
                        literal(1)
                    ),
                    `else` = literal(0)
                )
            ),
            `else` = literal(0)
        ).asc()

        val res = sort.map {
            val columnSpec = when (it.property) {
                "viewCount" -> col(Competition::viewCount)
                "createdAt" -> col(Competition::createdAt)
                "scrapCount" -> col(Competition::scrapCount)
                else -> col(Competition::viewCount)
            }

            if (it.isAscending) {
                columnSpec.asc()
            } else {
                columnSpec.desc()
            }
        }.toList()

        return listOf(endDate) + res
    }
}
