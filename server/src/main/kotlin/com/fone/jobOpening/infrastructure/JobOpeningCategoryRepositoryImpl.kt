package com.fone.jobOpening.infrastructure

import com.fone.common.entity.CategoryType
import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.spring.data.reactive.query.SpringDataHibernateMutinyReactiveQueryFactory
import com.linecorp.kotlinjdsl.spring.data.reactive.query.deleteQuery
import com.linecorp.kotlinjdsl.spring.data.reactive.query.listQuery
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.hibernate.reactive.mutiny.Mutiny
import org.springframework.stereotype.Repository

@Repository
class JobOpeningCategoryRepositoryImpl(
    private val sessionFactory: Mutiny.SessionFactory,
    private val queryFactory: SpringDataHibernateMutinyReactiveQueryFactory,
) : com.fone.jobOpening.domain.repository.JobOpeningCategoryRepository {

    override suspend fun saveAll(
        jobOpeningCategories: List<com.fone.jobOpening.domain.entity.JobOpeningCategory>,
    ): List<com.fone.jobOpening.domain.entity.JobOpeningCategory> {
        return jobOpeningCategories.also {
            sessionFactory.withSession { session ->
                session.persistAll(*it.toTypedArray()).flatMap { session.flush() }
            }.awaitSuspending()
        }
    }

    override suspend fun deleteByJobOpeningId(jobOpeningId: Long): Int {
        return queryFactory.deleteQuery<com.fone.jobOpening.domain.entity.JobOpeningCategory> {
            where(col(com.fone.jobOpening.domain.entity.JobOpeningCategory::jobOpeningId).equal(jobOpeningId))
        }
    }

    override suspend fun findByJobOpeningIds(
        jobOpeningIds: List<Long>,
    ): Map<Long, List<CategoryType>> {
        return queryFactory.listQuery {
            select(entity(com.fone.jobOpening.domain.entity.JobOpeningCategory::class))
            from(entity(com.fone.jobOpening.domain.entity.JobOpeningCategory::class))
            where(col(com.fone.jobOpening.domain.entity.JobOpeningCategory::jobOpeningId).`in`(jobOpeningIds))
        }.groupBy({ it!!.jobOpeningId }, { it!!.type })
    }

    override suspend fun findByJobOpeningId(jobOpeningId: Long): List<CategoryType> {
        return queryFactory.listQuery {
            select(col(com.fone.jobOpening.domain.entity.JobOpeningCategory::type))
            from(entity(com.fone.jobOpening.domain.entity.JobOpeningCategory::class))
            where(col(com.fone.jobOpening.domain.entity.JobOpeningCategory::jobOpeningId).equal(jobOpeningId))
        }
    }
}
