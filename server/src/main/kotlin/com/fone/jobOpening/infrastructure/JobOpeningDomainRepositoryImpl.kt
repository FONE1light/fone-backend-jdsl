package com.fone.jobOpening.infrastructure

import com.fone.common.entity.DomainType
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
) : com.fone.jobOpening.domain.repository.JobOpeningDomainRepository {

    override suspend fun saveAll(jobOpeningDomain: List<com.fone.jobOpening.domain.entity.JobOpeningDomain>): List<com.fone.jobOpening.domain.entity.JobOpeningDomain> {
        return jobOpeningDomain.also {
            sessionFactory.withSession { session ->
                session.persistAll(*it.toTypedArray()).flatMap { session.flush() }
            }.awaitSuspending()
        }
    }

    override suspend fun deleteByJobOpeningId(jobOpeningId: Long): Int {
        return queryFactory.deleteQuery<com.fone.jobOpening.domain.entity.JobOpeningDomain> {
            where(col(com.fone.jobOpening.domain.entity.JobOpeningDomain::jobOpeningId).equal(jobOpeningId))
        }
    }

    override suspend fun findByJobOpeningIds(
        jobOpeningIds: List<Long>,
    ): Map<Long, List<DomainType>> {
        return queryFactory.listQuery {
            select(entity(com.fone.jobOpening.domain.entity.JobOpeningDomain::class))
            from(entity(com.fone.jobOpening.domain.entity.JobOpeningDomain::class))
            where(col(com.fone.jobOpening.domain.entity.JobOpeningDomain::jobOpeningId).`in`(jobOpeningIds))
        }.groupBy({ it!!.jobOpeningId }, { it!!.type })
    }

    override suspend fun findByJobOpeningId(jobOpeningId: Long): List<DomainType> {
        return queryFactory.listQuery {
            select(col(com.fone.jobOpening.domain.entity.JobOpeningDomain::type))
            from(entity(com.fone.jobOpening.domain.entity.JobOpeningDomain::class))
            where(col(com.fone.jobOpening.domain.entity.JobOpeningDomain::jobOpeningId).equal(jobOpeningId))
        }
    }
}
