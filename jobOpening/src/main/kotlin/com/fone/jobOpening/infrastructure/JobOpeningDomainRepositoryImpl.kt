package com.fone.jobOpening.infrastructure

import com.fone.common.entity.DomainType
import com.fone.jobOpening.domain.entity.JobOpeningDomain
import com.fone.jobOpening.domain.repository.JobOpeningDomainRepository
import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.spring.data.reactive.query.SpringDataHibernateMutinyReactiveQueryFactory
import com.linecorp.kotlinjdsl.spring.data.reactive.query.deleteQuery
import com.linecorp.kotlinjdsl.spring.data.reactive.query.listQuery
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.hibernate.reactive.mutiny.Mutiny
import org.springframework.stereotype.Repository

@Repository
class JobOpeningDomainRepositoryImpl(
    private val sessionFactory: Mutiny.SessionFactory,
    private val queryFactory: SpringDataHibernateMutinyReactiveQueryFactory,
) : JobOpeningDomainRepository {

    override suspend fun saveAll(jobOpeningDomain: List<JobOpeningDomain>): List<JobOpeningDomain> {
        return jobOpeningDomain.also {
            sessionFactory
                .withSession { session ->
                    session.persistAll(*it.toTypedArray()).flatMap { session.flush() }
                }
                .awaitSuspending()
        }
    }

    override suspend fun deleteByJobOpeningId(jobOpeningId: Long): Int {
        return queryFactory.deleteQuery<JobOpeningDomain> {
            where(col(JobOpeningDomain::jobOpeningId).equal(jobOpeningId))
        }
    }

    override suspend fun findByJobOpeningIds(
        jobOpeningIds: List<Long>
    ): Map<Long, List<DomainType>> {
        return queryFactory
            .listQuery {
                select(entity(JobOpeningDomain::class))
                from(entity(JobOpeningDomain::class))
                where(col(JobOpeningDomain::jobOpeningId).`in`(jobOpeningIds))
            }
            .groupBy({ it!!.jobOpeningId }, { it!!.type })
    }

    override suspend fun findByJobOpeningId(jobOpeningId: Long): List<DomainType> {
        return queryFactory.listQuery {
            select(col(JobOpeningDomain::type))
            from(entity(JobOpeningDomain::class))
            where(col(JobOpeningDomain::jobOpeningId).equal(jobOpeningId))
        }
    }
}
