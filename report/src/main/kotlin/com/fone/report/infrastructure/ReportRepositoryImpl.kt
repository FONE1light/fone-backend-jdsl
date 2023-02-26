package com.fone.report.infrastructure

import com.fone.report.domain.entity.Report
import com.fone.report.domain.repository.ReportRepository
import com.linecorp.kotlinjdsl.spring.data.reactive.query.SpringDataHibernateMutinyReactiveQueryFactory
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.hibernate.reactive.mutiny.Mutiny
import org.springframework.stereotype.Repository

@Repository
class ReportRepositoryImpl(
    private val sessionFactory: Mutiny.SessionFactory,
    private val queryFactory: SpringDataHibernateMutinyReactiveQueryFactory,
) : ReportRepository {

    override suspend fun save(report: Report): Report {
        return report.also {
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
