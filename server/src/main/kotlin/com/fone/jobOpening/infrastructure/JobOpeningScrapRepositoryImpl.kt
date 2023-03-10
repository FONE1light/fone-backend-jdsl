package com.fone.jobOpening.infrastructure

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
) : com.fone.jobOpening.domain.repository.JobOpeningScrapRepository {

    override suspend fun findByUserIdAndJobOpeningId(
        userId: Long,
        jobOpeningId: Long,
    ): com.fone.jobOpening.domain.entity.JobOpeningScrap? {
        return queryFactory.singleQueryOrNull {
            select(entity(com.fone.jobOpening.domain.entity.JobOpeningScrap::class))
            from(entity(com.fone.jobOpening.domain.entity.JobOpeningScrap::class))
            where(
                and(
                    userIdEq(userId),
                    jobOpeningIdEq(jobOpeningId)
                )
            )
        }
    }

    override suspend fun findByUserId(userId: Long): Map<Long, com.fone.jobOpening.domain.entity.JobOpeningScrap?> {
        return queryFactory.listQuery {
            select(entity(com.fone.jobOpening.domain.entity.JobOpeningScrap::class))
            from(entity(com.fone.jobOpening.domain.entity.JobOpeningScrap::class))
            where(col(com.fone.jobOpening.domain.entity.JobOpeningScrap::userId).equal(userId))
        }.associateBy { it!!.jobOpeningId }
    }

    override suspend fun delete(jobOpeningScrap: com.fone.jobOpening.domain.entity.JobOpeningScrap): Int {
        return queryFactory.deleteQuery<com.fone.jobOpening.domain.entity.JobOpeningScrap> {
            where(jobOpeningScrapId(jobOpeningScrap))
        }
    }

    override suspend fun save(jobOpeningScrap: com.fone.jobOpening.domain.entity.JobOpeningScrap): com.fone.jobOpening.domain.entity.JobOpeningScrap {
        return jobOpeningScrap.also {
            queryFactory.withFactory { session, _ ->
                if (it.id == null) {
                    session.persist(it)
                } else {
                    session.merge(it)
                }.flatMap { session.flush() }.awaitSuspending()
            }
        }
    }

    private fun SpringDataReactiveCriteriaQueryDsl<com.fone.jobOpening.domain.entity.JobOpeningScrap?>.jobOpeningIdEq(
        jobOpeningId: Long,
    ) = col(com.fone.jobOpening.domain.entity.JobOpeningScrap::jobOpeningId).equal(jobOpeningId)

    private fun SpringDataReactiveCriteriaQueryDsl<com.fone.jobOpening.domain.entity.JobOpeningScrap?>.userIdEq(
        userId: Long,
    ) = col(com.fone.jobOpening.domain.entity.JobOpeningScrap::userId).equal(userId)

    private fun CriteriaDeleteQueryDsl.jobOpeningScrapId(
        jobOpeningScrap: com.fone.jobOpening.domain.entity.JobOpeningScrap,
    ) =
        col(com.fone.jobOpening.domain.entity.JobOpeningScrap::id).equal(jobOpeningScrap.id)
}
