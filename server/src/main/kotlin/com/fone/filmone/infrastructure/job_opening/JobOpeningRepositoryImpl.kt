package com.fone.filmone.infrastructure.job_opening

import com.fone.filmone.domain.common.Type
import com.fone.filmone.domain.job_opening.entity.JobOpening
import com.fone.filmone.domain.job_opening.entity.JobOpeningScrap
import com.fone.filmone.domain.job_opening.repository.JobOpeningRepository
import com.fone.filmone.presentation.job_opening.RetrieveJobOpeningDto.*
import com.linecorp.kotlinjdsl.query.spec.predicate.EqualValueSpec
import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.querydsl.expression.column
import com.linecorp.kotlinjdsl.spring.data.reactive.query.*
import com.linecorp.kotlinjdsl.spring.reactive.querydsl.SpringDataReactiveCriteriaQueryDsl
import com.linecorp.kotlinjdsl.spring.reactive.querydsl.SpringDataReactivePageableQueryDsl
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.hibernate.reactive.mutiny.Mutiny
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.stereotype.Repository

@Repository
class JobOpeningRepositoryImpl(
    private val sessionFactory: Mutiny.SessionFactory,
    private val queryFactory: SpringDataHibernateMutinyReactiveQueryFactory,
) : JobOpeningRepository {

    override suspend fun findAllTop5ByType(pageable: Pageable, type: Type): Slice<JobOpening> {
        return queryFactory.pageQuery(pageable) {
            select(entity(JobOpening::class))
            from(entity(JobOpening::class))
            where(
                and(
                    typeEq(type)
                )
            )
        }
    }

    override suspend fun findByFilters(
        pageable: Pageable,
        request: RetrieveJobOpeningsRequest,
    ): Slice<JobOpening> {
        return queryFactory.pageQuery(pageable) {
            select(entity(JobOpening::class))
            from(entity(JobOpening::class))
            where(
                and(
                    typeEq(request.type),
                    col(JobOpening::gender).equal(request.gender),
                    col(JobOpening::ageMax).lessThanOrEqualTo(request.ageMax),
                    col(JobOpening::ageMin).greaterThanOrEqualTo(request.ageMin),
                )
            )
        }
    }

    override suspend fun findByTypeAndId(type: Type?, jobOpeningId: Long?): JobOpening? {
        return queryFactory.singleQueryOrNull {
            select(entity(JobOpening::class))
            from(entity(JobOpening::class))
            where(
                and(
                    typeEqOrNull(type),
                    jobOpeningIdEq(jobOpeningId),
                )
            )
        }
    }

    override suspend fun findAllByUserId(pageable: Pageable, userId: Long): Slice<JobOpening> {
        return queryFactory.pageQuery(pageable) {
            select(entity(JobOpening::class))
            from(entity(JobOpening::class))
            where(userIdEq(userId))
        }
    }

    override suspend fun findScrapAllByUserId(
        pageable: Pageable,
        userId: Long,
        type: Type,
    ): Slice<JobOpening> {
        val jobOpeningIds = queryFactory.subquery {
            select(column(JobOpeningScrap::id))
            from(entity(JobOpeningScrap::class))
            where(col(JobOpeningScrap::userId).equal(userId))
        }

        return queryFactory.pageQuery(pageable) {
            select(entity(JobOpening::class))
            from(entity(JobOpening::class))
            where(
                and(
                    col(JobOpening::id).`in`(jobOpeningIds),
                    typeEq(type),
                )
            )
        }
    }

    override suspend fun save(jobOpening: JobOpening): JobOpening {
        return jobOpening.also {
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
}
