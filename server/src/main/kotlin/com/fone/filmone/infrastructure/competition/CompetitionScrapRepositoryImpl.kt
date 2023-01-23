package com.fone.filmone.infrastructure.competition

import com.fone.filmone.domain.competition.entity.CompetitionScrap
import com.fone.filmone.domain.competition.repository.CompetitionScrapRepository
import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.spring.data.reactive.query.SpringDataHibernateMutinyReactiveQueryFactory
import com.linecorp.kotlinjdsl.spring.data.reactive.query.deleteQuery
import com.linecorp.kotlinjdsl.spring.data.reactive.query.listQuery
import com.linecorp.kotlinjdsl.spring.data.reactive.query.singleQueryOrNull
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.hibernate.reactive.mutiny.Mutiny
import org.springframework.stereotype.Repository

@Repository
class CompetitionScrapRepositoryImpl(
    private val sessionFactory: Mutiny.SessionFactory,
    private val queryFactory: SpringDataHibernateMutinyReactiveQueryFactory,
) : CompetitionScrapRepository {

    override suspend fun findByUserIdAndCompetitionId(
        userId: Long,
        competitionId: Long,
    ): CompetitionScrap? {
        return queryFactory.singleQueryOrNull {
            select(entity(CompetitionScrap::class))
            from(entity(CompetitionScrap::class))
            where(
                and(
                    col(CompetitionScrap::userId).equal(userId),
                    col(CompetitionScrap::competitionId).equal(competitionId),
                )
            )
        }
    }

    override suspend fun findByUserId(userId: Long): List<CompetitionScrap> {
        return queryFactory.listQuery {
            select(entity(CompetitionScrap::class))
            from(entity(CompetitionScrap::class))
            where(col(CompetitionScrap::userId).equal(userId))
        }
    }

    override suspend fun delete(competitionScrap: CompetitionScrap): Int {
        return queryFactory.deleteQuery<CompetitionScrap> {
            where(col(CompetitionScrap::id).equal(competitionScrap.id))
        }
    }

    override suspend fun save(competitionScrap: CompetitionScrap): CompetitionScrap {
        return competitionScrap.also {
            sessionFactory.withSession { session ->
                session.persist(it).flatMap { session.flush() }
            }.awaitSuspending()
        }
    }
}