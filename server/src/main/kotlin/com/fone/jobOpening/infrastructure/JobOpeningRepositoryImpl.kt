package com.fone.jobOpening.infrastructure

import com.fone.common.entity.Type
import com.fone.jobOpening.domain.entity.JobOpening
import com.fone.jobOpening.domain.entity.JobOpeningCategory
import com.fone.jobOpening.domain.entity.JobOpeningDomain
import com.fone.jobOpening.domain.entity.JobOpeningScrap
import com.fone.jobOpening.domain.repository.JobOpeningRepository
import com.fone.jobOpening.presentation.dto.RetrieveJobOpeningDto.RetrieveJobOpeningsRequest
import com.linecorp.kotlinjdsl.query.spec.OrderSpec
import com.linecorp.kotlinjdsl.query.spec.predicate.EqualValueSpec
import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.querydsl.expression.column
import com.linecorp.kotlinjdsl.spring.data.reactive.query.SpringDataHibernateMutinyReactiveQueryFactory
import com.linecorp.kotlinjdsl.spring.data.reactive.query.listQuery
import com.linecorp.kotlinjdsl.spring.data.reactive.query.pageQuery
import com.linecorp.kotlinjdsl.spring.data.reactive.query.singleQueryOrNull
import com.linecorp.kotlinjdsl.spring.reactive.listQuery
import com.linecorp.kotlinjdsl.spring.reactive.pageQuery
import com.linecorp.kotlinjdsl.spring.reactive.querydsl.SpringDataReactiveCriteriaQueryDsl
import com.linecorp.kotlinjdsl.spring.reactive.querydsl.SpringDataReactivePageableQueryDsl
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.hibernate.reactive.mutiny.Mutiny
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class JobOpeningRepositoryImpl(
    private val sessionFactory: Mutiny.SessionFactory,
    private val queryFactory: SpringDataHibernateMutinyReactiveQueryFactory,
) : JobOpeningRepository {

    override suspend fun findAllTop5ByType(
        pageable: Pageable,
        type: Type,
    ): Page<JobOpening> {
        return queryFactory.withFactory { factory ->
            factory.pageQuery(pageable) {
                select(entity(JobOpening::class))
                from(entity(JobOpening::class))
                where(
                    and(
                        typeEq(type),
                        col(JobOpening::isDeleted).equal(false)
                    )
                )
            }
        }
    }

    override suspend fun findByFilters(
        pageable: Pageable,
        request: RetrieveJobOpeningsRequest,
    ): Page<JobOpening> {
        return queryFactory.withFactory { factory ->
            val domainJobOpeningIds = if (request.domains.isEmpty()) {
                emptyList()
            } else {
                factory.listQuery {
                    select(col(JobOpeningDomain::jobOpeningId))
                    from(entity(JobOpeningDomain::class))
                    where(
                        and(
                            col(JobOpeningDomain::type).`in`(request.domains)
                        )
                    )
                }
            }

            val categoryJobOpeningIds = if (request.categories.isEmpty()) {
                emptyList()
            } else {
                factory.listQuery {
                    select(col(JobOpeningCategory::jobOpeningId))
                    from(entity(JobOpeningCategory::class))
                    where(
                        and(
                            col(JobOpeningCategory::type).`in`(request.categories)
                        )
                    )
                }
            }

            val jobOpeningIds = queryFactory.pageQuery(pageable) {
                select(column(JobOpening::id))
                from(entity(JobOpening::class))
                where(
                    and(
                        col(JobOpening::type).equal(request.type),
                        col(JobOpening::gender).`in`(request.genders),
                        col(JobOpening::ageMax).greaterThanOrEqualTo(request.ageMin),
                        col(JobOpening::ageMin).lessThanOrEqualTo(request.ageMax),
                        if (request.domains.isNotEmpty()) col(JobOpening::id).`in`(domainJobOpeningIds) else null,
                        if (request.categories.isNotEmpty()) col(JobOpening::id).`in`(categoryJobOpeningIds) else null,
                        col(JobOpening::isDeleted).equal(false)
                    )
                )
            }

            if (jobOpeningIds.content.isEmpty()) {
                return@withFactory PageImpl(
                    listOf(),
                    pageable,
                    0
                )
            }

            val jobOpenings = queryFactory.listQuery {
                select(entity(JobOpening::class))
                from(entity(JobOpening::class))
                where(and(col(JobOpening::id).`in`(jobOpeningIds.content)))
                orderBy(orderSpec(pageable.sort))
            }

            PageImpl(
                jobOpenings,
                pageable,
                jobOpenings.size.toLong()
            )
        }
    }

    override suspend fun findById(
        jobOpeningId: Long,
    ): JobOpening? {
        return queryFactory.singleQueryOrNull {
            select(entity(JobOpening::class))
            from(entity(JobOpening::class))
            where(
                jobOpeningIdEq(jobOpeningId)
            )
        }
    }

    override suspend fun findByTypeAndId(
        type: Type?,
        jobOpeningId: Long?,
    ): JobOpening? {
        return queryFactory.singleQueryOrNull {
            select(entity(JobOpening::class))
            from(entity(JobOpening::class))
            where(
                and(
                    typeEqOrNull(type),
                    jobOpeningIdEq(jobOpeningId),
                    col(JobOpening::isDeleted).equal(false)
                )
            )
        }
    }

    override suspend fun findAllByUserId(
        pageable: Pageable,
        userId: Long,
    ): Page<JobOpening> {
        return queryFactory.pageQuery(pageable) {
            select(entity(JobOpening::class))
            from(entity(JobOpening::class))
            where(
                and(
                    userIdEq(userId),
                    col(JobOpening::isDeleted).equal(false)
                )
            )
        }
    }

    override suspend fun findScrapAllByUserId(
        pageable: Pageable,
        userId: Long,
        type: Type?,
    ): Page<JobOpening> {
        return queryFactory.withFactory { factory ->
            val ids = factory.pageQuery(pageable) {
                select(column(JobOpeningScrap::jobOpeningId))
                from(entity(JobOpeningScrap::class))
                join(entity(JobOpening::class), col(JobOpening::id).equal(col(JobOpeningScrap::jobOpeningId)))
                where(
                    and(
                        col(JobOpeningScrap::userId).equal(userId),
                        if (type != null) col(JobOpening::type).equal(type) else null
                    )
                )
            }

            val jobOpenings = factory.listQuery {
                select(entity(JobOpening::class))
                from(entity(JobOpening::class))
                where(col(JobOpening::id).`in`(ids.content))
            }.associateBy { it?.id }

            ids.map { jobOpenings[it] }
        }
    }

    override suspend fun save(jobOpening: JobOpening): JobOpening {
        val test = jobOpening.also {
            queryFactory.withFactory { session, _ ->
                if (it.id == null) {
                    session.persist(it)
                } else {
                    session.merge(it)
                }.flatMap { session.flush() }.awaitSuspending()
            }
        }

        return test
    }

    private fun SpringDataReactiveCriteriaQueryDsl<JobOpening?>.jobOpeningIdEq(
        jobOpeningId: Long?,
    ) = col(JobOpening::id).equal(jobOpeningId)

    private fun SpringDataReactivePageableQueryDsl<JobOpening>.idIn(
        jobOpeningIds: List<Long>,
    ) = col(JobOpening::id).`in`(jobOpeningIds)

    private fun SpringDataReactivePageableQueryDsl<JobOpening>.userIdEq(
        userId: Long,
    ) = col(JobOpening::userId).equal(userId)

    private fun SpringDataReactiveCriteriaQueryDsl<JobOpening?>.typeEqOrNull(
        type: Type?,
    ): EqualValueSpec<Type>? {
        type ?: return null

        return col(JobOpening::type).equal(type)
    }

    private fun SpringDataReactiveCriteriaQueryDsl<JobOpening>.typeEq(
        type: Type?,
    ): EqualValueSpec<Type>? {
        type ?: return null

        return col(JobOpening::type).equal(type)
    }

    private fun SpringDataReactivePageableQueryDsl<JobOpening>.typeEq(
        type: Type?,
    ): EqualValueSpec<Type>? {
        type ?: return null
        return col(JobOpening::type).equal(type)
    }

    private fun SpringDataReactiveCriteriaQueryDsl<JobOpening?>.orderSpec(
        sort: Sort,
    ): List<OrderSpec> {
        val deadlineIsNull = case(
            `when`(column(JobOpening::deadline).isNull()).then(
                literal(1)
            ),
            `else` = literal(0)
        ).asc()

        val deadlineAfterToday = case(
            `when`(column(JobOpening::deadline).greaterThan(LocalDate.now())).then(
                literal(1)
            ),
            `else` = literal(0)
        ).desc()

        val deadlineDesc = column(JobOpening::deadline).asc()

        val res = sort.map {
            val columnSpec = when (it.property) {
                "viewCount" -> col(JobOpening::viewCount)
                "createdAt" -> col(JobOpening::createdAt)
                "scrapCount" -> col(JobOpening::scrapCount)
                else -> col(JobOpening::viewCount)
            }

            if (it.isAscending) {
                columnSpec.asc()
            } else {
                columnSpec.desc()
            }
        }.toList()

        return if (sort.map { it.property }.contains("deadline")) {
            listOf(deadlineIsNull, deadlineAfterToday, deadlineDesc)
        } else {
            res
        }
    }
}
