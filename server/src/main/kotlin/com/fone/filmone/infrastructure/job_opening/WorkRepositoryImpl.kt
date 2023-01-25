package com.fone.filmone.infrastructure.job_opening

import com.fone.filmone.domain.job_opening.entity.Work
import com.fone.filmone.domain.job_opening.repository.WorkRepository
import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.spring.data.reactive.query.SpringDataHibernateMutinyReactiveQueryFactory
import com.linecorp.kotlinjdsl.spring.data.reactive.query.singleQueryOrNull
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.hibernate.reactive.mutiny.Mutiny
import org.springframework.stereotype.Repository

@Repository
class WorkRepositoryImpl(
    private val sessionFactory: Mutiny.SessionFactory,
    private val queryFactory: SpringDataHibernateMutinyReactiveQueryFactory,
) : WorkRepository {

    override suspend fun findByJobOpeningId(jobOpeningId: Long): Work? {
        return queryFactory.singleQueryOrNull {
            select(entity(Work::class))
            from(entity(Work::class))
            where(col(Work::jobOpeningId).equal(jobOpeningId))
        }
    }

    override suspend fun save(work: Work): Work {
        return work.also {
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
}