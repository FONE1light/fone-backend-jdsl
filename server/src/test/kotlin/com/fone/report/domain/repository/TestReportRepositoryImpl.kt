package com.fone.report.domain.repository

import com.fone.report.domain.entity.Report
import com.linecorp.kotlinjdsl.querydsl.expression.column
import com.linecorp.kotlinjdsl.spring.data.reactive.query.SpringDataHibernateMutinyReactiveQueryFactory
import com.linecorp.kotlinjdsl.spring.data.reactive.query.singleQuery
import com.linecorp.kotlinjdsl.spring.reactive.SpringDataReactiveQueryFactory
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.hibernate.reactive.mutiny.Mutiny
import org.hibernate.reactive.mutiny.Mutiny.Session
import org.springframework.context.annotation.Primary
import org.springframework.stereotype.Repository

@Repository
@Primary
class TestReportRepositoryImpl(
    private val sessionFactory: Mutiny.SessionFactory,
    private val queryFactory: SpringDataHibernateMutinyReactiveQueryFactory,
) : ReportRepository {
    suspend fun getCount(): Long = queryFactory.singleQuery {
        select(count(column(Report::id)))
        from(Report::class)
    }

    suspend fun startTransaction(
        transactionScope: suspend (RepositorySession, SpringDataReactiveQueryFactory) -> Unit,
    ) {
        queryFactory.transactionWithFactory { session, factory ->
            transactionScope(RepositorySession(session), factory)
        }
    }

    override suspend fun save(report: Report): Report {
        return report.also {
            queryFactory.withFactory { session, _ ->
                if (it.id == null) {
                    session.persist(it)
                } else {
                    session.merge(it)
                }.flatMap { session.flush() }.awaitSuspending()
            }
        }
    }

    companion object {
        class RepositorySession(session: Session) : Session by session
    }
}

suspend fun TestReportRepositoryImpl.Companion.RepositorySession.save(report: Report): Report {
    if (report.id == null) {
        this.persist(report)
    } else {
        this.merge(report)
    }.flatMap { this.flush() }.awaitSuspending()
    return report
}
