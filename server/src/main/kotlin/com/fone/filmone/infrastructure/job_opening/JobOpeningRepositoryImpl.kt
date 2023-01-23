package com.fone.filmone.infrastructure.job_opening

import com.fone.filmone.domain.common.Type
import com.fone.filmone.domain.job_opening.entity.JobOpening
import com.fone.filmone.domain.job_opening.repository.JobOpeningRepository
import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.spring.data.reactive.query.SpringDataHibernateMutinyReactiveQueryFactory
import com.linecorp.kotlinjdsl.spring.data.reactive.query.listQuery
import com.linecorp.kotlinjdsl.spring.data.reactive.query.pageQuery
import com.linecorp.kotlinjdsl.spring.data.reactive.query.singleQueryOrNull
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

    override suspend fun findTop5ByType(type: Type): List<JobOpening> {
        return queryFactory.listQuery {
            select(entity(JobOpening::class))
            from(entity(JobOpening::class))
            where(col(JobOpening::type).equal(type))
        }
    }

    override suspend fun findByType(pageable: Pageable, type: Type): Slice<JobOpening> {
        return queryFactory.pageQuery(pageable) {
            select(entity(JobOpening::class))
            from(entity(JobOpening::class))
            where(col(JobOpening::type).equal(type))
        }
    }

    override suspend fun findByType(type: Type): JobOpening? {
        return queryFactory.singleQueryOrNull {
            select(entity(JobOpening::class))
            from(entity(JobOpening::class))
            where(col(JobOpening::type).equal(type))
        }
    }

    override suspend fun findByUserId(userId: Long): List<JobOpening> {
        return queryFactory.listQuery {
            select(entity(JobOpening::class))
            from(entity(JobOpening::class))
            where(col(JobOpening::userId).equal(userId))
        }
    }

    override suspend fun findById(jobOpeningId: Long): JobOpening? {
        return queryFactory.singleQueryOrNull {
            select(entity(JobOpening::class))
            from(entity(JobOpening::class))
            where(col(JobOpening::id).equal(jobOpeningId))
        }
    }

    override suspend fun save(jobOpening: JobOpening): JobOpening {
        return jobOpening.also {
            sessionFactory.withSession { session ->
                session.persist(it).flatMap { session.flush() }
            }.awaitSuspending()
        }
    }

    override suspend fun findAllById(jobOpeningIds: List<Long>): List<JobOpening> {
        return queryFactory.listQuery {
            select(entity(JobOpening::class))
            from(entity(JobOpening::class))
            where(col(JobOpening::id).`in`(jobOpeningIds))
        }
    }
}