package com.fone.jobOpening.infrastructure

import com.fone.common.config.jpa.inValues
import com.fone.common.entity.CategoryType
import com.fone.jobOpening.domain.entity.JobOpeningCategory
import com.fone.jobOpening.domain.repository.JobOpeningCategoryRepository
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
) : JobOpeningCategoryRepository {
    override suspend fun saveAll(jobOpeningCategories: List<JobOpeningCategory>): List<JobOpeningCategory> {
        return jobOpeningCategories.also {
            sessionFactory.withSession { session ->
                session.persistAll(*it.toTypedArray()).flatMap { session.flush() }
            }.awaitSuspending()
        }
    }

    override suspend fun deleteByJobOpeningId(jobOpeningId: Long): Int {
        return queryFactory.deleteQuery<JobOpeningCategory> {
            where(col(JobOpeningCategory::jobOpeningId).equal(jobOpeningId))
        }
    }

    override suspend fun findByJobOpeningIds(jobOpeningIds: List<Long>): Map<Long, List<CategoryType>> {
        return queryFactory.listQuery {
            select(entity(JobOpeningCategory::class))
            from(entity(JobOpeningCategory::class))
            where(col(JobOpeningCategory::jobOpeningId).inValues(jobOpeningIds))
        }.groupBy({ it!!.jobOpeningId }, { it!!.type })
    }

    override suspend fun findByJobOpeningId(jobOpeningId: Long): List<CategoryType> {
        return queryFactory.listQuery {
            select(col(JobOpeningCategory::type))
            from(entity(JobOpeningCategory::class))
            where(col(JobOpeningCategory::jobOpeningId).equal(jobOpeningId))
        }
    }
}
