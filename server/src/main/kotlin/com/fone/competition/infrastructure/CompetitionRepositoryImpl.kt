package com.fone.competition.infrastructure

import com.fone.common.config.jpa.inValues
import com.fone.competition.domain.entity.Competition
import com.fone.competition.domain.entity.CompetitionScrap
import com.fone.competition.domain.repository.CompetitionRepository
import com.linecorp.kotlinjdsl.query.spec.OrderSpec
import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.querydsl.expression.column
import com.linecorp.kotlinjdsl.querydsl.from.join
import com.linecorp.kotlinjdsl.spring.data.reactive.query.SpringDataHibernateMutinyReactiveQueryFactory
import com.linecorp.kotlinjdsl.spring.data.reactive.query.singleQuery
import com.linecorp.kotlinjdsl.spring.reactive.listQuery
import com.linecorp.kotlinjdsl.spring.reactive.pageQuery
import com.linecorp.kotlinjdsl.spring.reactive.querydsl.SpringDataReactiveCriteriaQueryDsl
import com.linecorp.kotlinjdsl.spring.reactive.singleQueryOrNull
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.hibernate.reactive.mutiny.Mutiny
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class CompetitionRepositoryImpl(
    private val sessionFactory: Mutiny.SessionFactory,
    private val queryFactory: SpringDataHibernateMutinyReactiveQueryFactory,
) : CompetitionRepository {
    override suspend fun findAll(pageable: Pageable): Page<Competition> {
        return queryFactory.withFactory { factory ->
            val ids =
                factory.pageQuery(pageable) {
                    select(column(Competition::id))
                    from(entity(Competition::class))
                    where(
                        col(Competition::showStartDate).lessThanOrEqualTo(
                            LocalDate.now()
                        )
                    )
                }

            val competitions =
                factory.listQuery {
                    select(entity(Competition::class))
                    from(entity(Competition::class))
                    where(
                        and(
                            col(Competition::id).inValues(ids.content),
                            col(Competition::showStartDate).lessThanOrEqualTo(
                                LocalDate.now()
                            )
                        )
                    )
                    orderBy(
                        orderSpec(pageable.sort)
                    )
                }.iterator()

            ids.map { competitions.next() }
        }
    }

    override suspend fun count(): Long {
        return queryFactory.singleQuery {
            select(count(column(Competition::id)))
            from(entity(Competition::class))
        }
    }

    override suspend fun findById(competitionId: Long): Competition? {
        return queryFactory.withFactory { factory ->
            factory.singleQueryOrNull {
                select(entity(Competition::class))
                from(entity(Competition::class))
                where(col(Competition::id).equal(competitionId))
            }
        }
    }

    override suspend fun findScrapAllById(
        pageable: Pageable,
        userId: Long,
    ): Page<Competition> {
        return queryFactory.withFactory { factory ->
            val ids =
                factory.pageQuery(pageable) {
                    select(column(Competition::id))
                    from(entity(CompetitionScrap::class))
                    join(CompetitionScrap::competition)
                    where(col(CompetitionScrap::userId).equal(userId))
                }

            val competitions =
                factory.listQuery {
                    select(entity(Competition::class))
                    from(entity(Competition::class))
                    where(col(Competition::id).inValues(ids.content))
                }.associateBy { it!!.id }

            ids.map { competitions[it] }
        }
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
        val screeningDateAfterToday =
            case(
                `when`(
                    column(Competition::screeningEndDate).greaterThan(LocalDate.now())
                ).then(
                    literal(1)
                ),
                `else` = literal(0)
            ).desc()

        val screeningDateIsNull =
            case(
                `when`(
                    or(
                        column(Competition::screeningEndDate).isNull(),
                        column(Competition::screeningStartDate).isNull()
                    )
                ).then(
                    literal(1)
                ),
                `else` = literal(0)
            ).desc()

        val screeningDateAsc = column(Competition::screeningEndDate).asc()

        val res =
            sort.map {
                val columnSpec =
                    when (it.property) {
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

        return if (sort.map { it.property }.contains("screeningEndDate")) {
            listOf(screeningDateAfterToday, screeningDateIsNull, screeningDateAsc)
        } else {
            res
        }
    }
}
