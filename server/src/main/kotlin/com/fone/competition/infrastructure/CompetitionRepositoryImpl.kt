package com.fone.competition.infrastructure

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
) : com.fone.competition.domain.repository.CompetitionRepository {

    override suspend fun findAll(pageable: Pageable): Slice<com.fone.competition.domain.entity.Competition> {
        val ids =
            queryFactory
                .pageQuery(pageable) {
                    select(column(com.fone.competition.domain.entity.Competition::id))
                    from(entity(com.fone.competition.domain.entity.Competition::class))
                }
                .content

        val competitions =
            queryFactory.listQuery {
                select(entity(com.fone.competition.domain.entity.Competition::class))
                from(entity(com.fone.competition.domain.entity.Competition::class))
                fetch(
                    com.fone.competition.domain.entity.Competition::class,
                    com.fone.competition.domain.entity.Prize::class,
                    on(
                        com.fone.competition.domain.entity.Competition::prizes
                    )
                )
                where(
                    and(
                        col(com.fone.competition.domain.entity.Competition::id).`in`(ids),
                        col(com.fone.competition.domain.entity.Competition::showStartDate).lessThanOrEqualTo(
                            LocalDate.now()
                        )
                    )
                )
                orderBy(
                    orderSpec(pageable.sort)
                )
            }

        return PageImpl(competitions, pageable, competitions.size.toLong())
    }

    override suspend fun count(): Long {
        return queryFactory.singleQuery {
            select(count(column(com.fone.competition.domain.entity.Competition::id)))
            from(entity(com.fone.competition.domain.entity.Competition::class))
        }
    }

    override suspend fun findById(competitionId: Long): com.fone.competition.domain.entity.Competition? {
        return queryFactory.singleQuery {
            select(entity(com.fone.competition.domain.entity.Competition::class))
            from(entity(com.fone.competition.domain.entity.Competition::class))
            fetch(com.fone.competition.domain.entity.Competition::prizes)
            where(col(com.fone.competition.domain.entity.Competition::id).equal(competitionId))
        }
    }

    override suspend fun findScrapAllById(
        pageable: Pageable,
        userId: Long,
    ): Slice<com.fone.competition.domain.entity.Competition> {
        val ids =
            queryFactory
                .pageQuery(pageable) {
                    select(column(com.fone.competition.domain.entity.Competition::id))
                    from(entity(com.fone.competition.domain.entity.CompetitionScrap::class))
                    join(com.fone.competition.domain.entity.CompetitionScrap::competition)
                    where(col(com.fone.competition.domain.entity.CompetitionScrap::userId).equal(userId))
                }
                .content

        val competitions =
            queryFactory.listQuery {
                select(entity(com.fone.competition.domain.entity.Competition::class))
                from(entity(com.fone.competition.domain.entity.Competition::class))
                fetch(com.fone.competition.domain.entity.Competition::prizes)
                where(col(com.fone.competition.domain.entity.Competition::id).`in`(ids))
            }

        return PageImpl(competitions, pageable, competitions.size.toLong())
    }

    override suspend fun save(competition: com.fone.competition.domain.entity.Competition): com.fone.competition.domain.entity.Competition {
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

    private fun SpringDataReactiveCriteriaQueryDsl<com.fone.competition.domain.entity.Competition?>.orderSpec(
        sort: Sort,
    ): List<OrderSpec> {
        val endDate =
            case(
                `when`(
                    column(com.fone.competition.domain.entity.Competition::submitEndDate).lessThanOrEqualTo(
                        LocalDate.now()
                    )
                )
                    .then(literal(1)),
                `when`(column(com.fone.competition.domain.entity.Competition::submitEndDate).isNull())
                    .then(
                        case(
                            `when`(
                                column(com.fone.competition.domain.entity.Competition::endDate)
                                    .lessThanOrEqualTo(LocalDate.now())
                            )
                                .then(literal(1)),
                            `else` = literal(0)
                        )
                    ),
                `else` = literal(0)
            )
                .asc()

        val res =
            sort
                .map {
                    val columnSpec =
                        when (it.property) {
                            "viewCount" -> col(com.fone.competition.domain.entity.Competition::viewCount)
                            "createdAt" -> col(com.fone.competition.domain.entity.Competition::createdAt)
                            "scrapCount" -> col(com.fone.competition.domain.entity.Competition::scrapCount)
                            else -> col(com.fone.competition.domain.entity.Competition::viewCount)
                        }

                    if (it.isAscending) {
                        columnSpec.asc()
                    } else {
                        columnSpec.desc()
                    }
                }
                .toList()

        return listOf(endDate) + res
    }
}
