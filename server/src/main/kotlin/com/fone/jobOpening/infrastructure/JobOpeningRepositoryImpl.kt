package com.fone.jobOpening.infrastructure

import com.fone.common.entity.Type
import com.fone.jobOpening.presentation.dto.RetrieveJobOpeningDto.RetrieveJobOpeningsRequest
import com.linecorp.kotlinjdsl.query.spec.OrderSpec
import com.linecorp.kotlinjdsl.query.spec.predicate.EqualValueSpec
import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.querydsl.expression.column
import com.linecorp.kotlinjdsl.spring.data.reactive.query.SpringDataHibernateMutinyReactiveQueryFactory
import com.linecorp.kotlinjdsl.spring.data.reactive.query.listQuery
import com.linecorp.kotlinjdsl.spring.data.reactive.query.pageQuery
import com.linecorp.kotlinjdsl.spring.data.reactive.query.singleQueryOrNull
import com.linecorp.kotlinjdsl.spring.data.reactive.query.subquery
import com.linecorp.kotlinjdsl.spring.reactive.querydsl.SpringDataReactiveCriteriaQueryDsl
import com.linecorp.kotlinjdsl.spring.reactive.querydsl.SpringDataReactivePageableQueryDsl
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.hibernate.reactive.mutiny.Mutiny
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Repository
import java.time.LocalDate

@Repository
class JobOpeningRepositoryImpl(
    private val sessionFactory: Mutiny.SessionFactory,
    private val queryFactory: SpringDataHibernateMutinyReactiveQueryFactory,
) : com.fone.jobOpening.domain.repository.JobOpeningRepository {

    override suspend fun findAllTop5ByType(
        pageable: Pageable,
        type: Type,
    ): Slice<com.fone.jobOpening.domain.entity.JobOpening> {
        return queryFactory.pageQuery(pageable) {
            select(entity(com.fone.jobOpening.domain.entity.JobOpening::class))
            from(entity(com.fone.jobOpening.domain.entity.JobOpening::class))
            where(
                and(
                    typeEq(type),
                    col(com.fone.jobOpening.domain.entity.JobOpening::isDeleted).equal(false)
                )
            )
        }
    }

    override suspend fun findByFilters(
        pageable: Pageable,
        request: RetrieveJobOpeningsRequest,
    ): Slice<com.fone.jobOpening.domain.entity.JobOpening> {
        val domainJobOpeningIds = queryFactory.listQuery {
            select(col(com.fone.jobOpening.domain.entity.JobOpeningDomain::jobOpeningId))
            from(entity(com.fone.jobOpening.domain.entity.JobOpeningDomain::class))
            where(
                and(
                    col(com.fone.jobOpening.domain.entity.JobOpeningDomain::type).`in`(request.domains)
                )
            )
        }

        val categoryJobOpeningIds = queryFactory.listQuery {
            select(col(com.fone.jobOpening.domain.entity.JobOpeningCategory::jobOpeningId))
            from(entity(com.fone.jobOpening.domain.entity.JobOpeningCategory::class))
            where(
                and(
                    col(com.fone.jobOpening.domain.entity.JobOpeningCategory::type).`in`(request.categories)
                )
            )
        }

        if (domainJobOpeningIds.isEmpty() || categoryJobOpeningIds.isEmpty()) {
            return PageImpl(
                listOf(),
                pageable,
                0
            )
        }

        val ids = queryFactory.pageQuery(pageable) {
            select(column(com.fone.jobOpening.domain.entity.JobOpening::id))
            from(entity(com.fone.jobOpening.domain.entity.JobOpening::class))
            where(
                and(
                    col(com.fone.jobOpening.domain.entity.JobOpening::type).equal(request.type),
                    col(com.fone.jobOpening.domain.entity.JobOpening::gender).`in`(request.genders),
                    or(
                        col(com.fone.jobOpening.domain.entity.JobOpening::ageMax).greaterThanOrEqualTo(request.ageMin),
                        col(com.fone.jobOpening.domain.entity.JobOpening::ageMin).lessThanOrEqualTo(request.ageMax)
                    ),
                    col(com.fone.jobOpening.domain.entity.JobOpening::id).`in`(domainJobOpeningIds),
                    col(com.fone.jobOpening.domain.entity.JobOpening::id).`in`(categoryJobOpeningIds),
                    col(com.fone.jobOpening.domain.entity.JobOpening::isDeleted).equal(false)
                )
            )
        }.content

        val jobOpenings = queryFactory.listQuery {
            select(entity(com.fone.jobOpening.domain.entity.JobOpening::class))
            from(entity(com.fone.jobOpening.domain.entity.JobOpening::class))
            where(col(com.fone.jobOpening.domain.entity.JobOpening::id).`in`(ids))
            orderBy(
                orderSpec(pageable.sort)
            )
        }

        return PageImpl(jobOpenings, pageable, jobOpenings.size.toLong())
    }

    override suspend fun findByTypeAndId(
        type: Type?,
        jobOpeningId: Long?,
    ): com.fone.jobOpening.domain.entity.JobOpening? {
        return queryFactory.singleQueryOrNull {
            select(entity(com.fone.jobOpening.domain.entity.JobOpening::class))
            from(entity(com.fone.jobOpening.domain.entity.JobOpening::class))
            where(
                and(
                    typeEqOrNull(type),
                    jobOpeningIdEq(jobOpeningId),
                    col(com.fone.jobOpening.domain.entity.JobOpening::isDeleted).equal(false)
                )
            )
        }
    }

    override suspend fun findAllByUserId(
        pageable: Pageable,
        userId: Long,
    ): Slice<com.fone.jobOpening.domain.entity.JobOpening> {
        return queryFactory.pageQuery(pageable) {
            select(entity(com.fone.jobOpening.domain.entity.JobOpening::class))
            from(entity(com.fone.jobOpening.domain.entity.JobOpening::class))
            where(
                and(
                    userIdEq(userId),
                    col(com.fone.jobOpening.domain.entity.JobOpening::isDeleted).equal(false)
                )
            )
        }
    }

    override suspend fun findScrapAllByUserId(
        pageable: Pageable,
        userId: Long,
        type: Type,
    ): Slice<com.fone.jobOpening.domain.entity.JobOpening> {
        val jobOpeningIds = queryFactory.subquery {
            select(column(com.fone.jobOpening.domain.entity.JobOpeningScrap::id))
            from(entity(com.fone.jobOpening.domain.entity.JobOpeningScrap::class))
            where(col(com.fone.jobOpening.domain.entity.JobOpeningScrap::userId).equal(userId))
        }

        return queryFactory.pageQuery(pageable) {
            select(entity(com.fone.jobOpening.domain.entity.JobOpening::class))
            from(entity(com.fone.jobOpening.domain.entity.JobOpening::class))
            where(
                and(
                    col(com.fone.jobOpening.domain.entity.JobOpening::id).`in`(jobOpeningIds),
                    typeEq(type),
                    col(com.fone.jobOpening.domain.entity.JobOpening::isDeleted).equal(false)
                )
            )
        }
    }

    override suspend fun save(jobOpening: com.fone.jobOpening.domain.entity.JobOpening): com.fone.jobOpening.domain.entity.JobOpening {
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

    private fun SpringDataReactiveCriteriaQueryDsl<com.fone.jobOpening.domain.entity.JobOpening?>.jobOpeningIdEq(
        jobOpeningId: Long?,
    ) = col(com.fone.jobOpening.domain.entity.JobOpening::id).equal(jobOpeningId)

    private fun SpringDataReactivePageableQueryDsl<com.fone.jobOpening.domain.entity.JobOpening>.idIn(
        jobOpeningIds: List<Long>,
    ) = col(com.fone.jobOpening.domain.entity.JobOpening::id).`in`(jobOpeningIds)

    private fun SpringDataReactivePageableQueryDsl<com.fone.jobOpening.domain.entity.JobOpening>.userIdEq(
        userId: Long,
    ) = col(com.fone.jobOpening.domain.entity.JobOpening::userId).equal(userId)

    private fun SpringDataReactiveCriteriaQueryDsl<com.fone.jobOpening.domain.entity.JobOpening?>.typeEqOrNull(
        type: Type?,
    ): EqualValueSpec<Type>? {
        type ?: return null

        return col(com.fone.jobOpening.domain.entity.JobOpening::type).equal(type)
    }

    private fun SpringDataReactiveCriteriaQueryDsl<com.fone.jobOpening.domain.entity.JobOpening>.typeEq(
        type: Type?,
    ): EqualValueSpec<Type>? {
        type ?: return null

        return col(com.fone.jobOpening.domain.entity.JobOpening::type).equal(type)
    }

    private fun SpringDataReactivePageableQueryDsl<com.fone.jobOpening.domain.entity.JobOpening>.typeEq(
        type: Type?,
    ): EqualValueSpec<Type>? {
        type ?: return null
        return col(com.fone.jobOpening.domain.entity.JobOpening::type).equal(type)
    }

    private fun SpringDataReactiveCriteriaQueryDsl<com.fone.jobOpening.domain.entity.JobOpening?>.orderSpec(
        sort: Sort,
    ): List<OrderSpec> {
        val endDate = case(
            `when`(column(com.fone.jobOpening.domain.entity.JobOpening::deadline).lessThanOrEqualTo(LocalDate.now())).then(
                literal(1)
            ),
            `else` = literal(0)
        ).asc()

        val res = sort.map {
            val columnSpec = when (it.property) {
                "viewCount" -> col(com.fone.jobOpening.domain.entity.JobOpening::viewCount)
                "createdAt" -> col(com.fone.jobOpening.domain.entity.JobOpening::createdAt)
                "scrapCount" -> col(com.fone.jobOpening.domain.entity.JobOpening::scrapCount)
                else -> col(com.fone.jobOpening.domain.entity.JobOpening::viewCount)
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
