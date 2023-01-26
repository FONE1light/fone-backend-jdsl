package com.fone.filmone.infrastructure.job_opening

import com.fone.filmone.domain.job_opening.entity.JobOpeningScrap
import com.fone.filmone.domain.job_opening.repository.JobOpeningScrapRepository
import com.linecorp.kotlinjdsl.querydsl.CriteriaDeleteQueryDsl
import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.spring.data.reactive.query.SpringDataHibernateMutinyReactiveQueryFactory
import com.linecorp.kotlinjdsl.spring.data.reactive.query.deleteQuery
import com.linecorp.kotlinjdsl.spring.data.reactive.query.listQuery
import com.linecorp.kotlinjdsl.spring.data.reactive.query.singleQueryOrNull
import com.linecorp.kotlinjdsl.spring.reactive.querydsl.SpringDataReactiveCriteriaQueryDsl
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.hibernate.reactive.mutiny.Mutiny
import org.springframework.stereotype.Repository

@Repository
class JobOpeningScrapRepositoryImpl(
    private val sessionFactory: Mutiny.SessionFactory,
    private val queryFactory: SpringDataHibernateMutinyReactiveQueryFactory,
) : JobOpeningScrapRepository {

    override suspend fun findByUserIdAndJobOpeningId(
        userId: Long,
        jobOpeningId: Long,
    ): JobOpeningScrap? {
        return queryFactory.singleQueryOrNull {
            select(entity(JobOpeningScrap::class))
            from(entity(JobOpeningScrap::class))
            where(
                and(
                    userIdEq(userId),
                    jobOpeningIdEq(jobOpeningId),
                )
            )
        }
    }

    override suspend fun findByUserId(userId: Long): Map<Long, JobOpeningScrap?> {

        return queryFactory.listQuery {
            select(entity(JobOpeningScrap::class))
            from(entity(JobOpeningScrap::class))
            where(col(JobOpeningScrap::userId).equal(userId))
        }.associateBy { it!!.jobOpeningId }
    }

    override suspend fun delete(jobOpeningScrap: JobOpeningScrap): Int {
        return queryFactory.deleteQuery<JobOpeningScrap> {
            where(jobOpeningScrapId(jobOpeningScrap))
        }
    }

    override suspend fun save(jobOpeningScrap: JobOpeningScrap): JobOpeningScrap {
        return jobOpeningScrap.also {
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

    private fun SpringDataReactiveCriteriaQueryDsl<JobOpeningScrap?>.jobOpeningIdEq(
        jobOpeningId: Long,
    ) = col(JobOpeningScrap::jobOpeningId).equal(jobOpeningId)

    private fun SpringDataReactiveCriteriaQueryDsl<JobOpeningScrap?>.userIdEq(
        userId: Long,
    ) = col(JobOpeningScrap::userId).equal(userId)

    private fun CriteriaDeleteQueryDsl.jobOpeningScrapId(jobOpeningScrap: JobOpeningScrap) =
        col(JobOpeningScrap::id).equal(jobOpeningScrap.id)
}