package com.fone.filmone.infrastructure.job_opening

import com.fone.filmone.domain.job_opening.entity.JobOpeningScrap
import com.fone.filmone.domain.job_opening.repository.JobOpeningScrapRepository
import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.spring.data.reactive.query.SpringDataHibernateMutinyReactiveQueryFactory
import com.linecorp.kotlinjdsl.spring.data.reactive.query.deleteQuery
import com.linecorp.kotlinjdsl.spring.data.reactive.query.listQuery
import com.linecorp.kotlinjdsl.spring.data.reactive.query.singleQueryOrNull
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
                    col(JobOpeningScrap::userId).equal(userId),
                    col(JobOpeningScrap::jobOpeningId).equal(jobOpeningId),
                )
            )
        }
    }

    override suspend fun findByUserId(userId: Long): List<JobOpeningScrap> {
        return queryFactory.listQuery {
            select(entity(JobOpeningScrap::class))
            from(entity(JobOpeningScrap::class))
            where(col(JobOpeningScrap::userId).equal(userId))
        }
    }

    override suspend fun delete(jobOpeningScrap: JobOpeningScrap): Int {
        return queryFactory.deleteQuery<JobOpeningScrap> {
            where(col(JobOpeningScrap::id).equal(jobOpeningScrap.id))
        }
    }

    override suspend fun save(jobOpeningScrap: JobOpeningScrap): JobOpeningScrap {
        return jobOpeningScrap.also {
            sessionFactory.withSession { session ->
                session.persist(it).flatMap { session.flush() }
            }.awaitSuspending()
        }
    }
}