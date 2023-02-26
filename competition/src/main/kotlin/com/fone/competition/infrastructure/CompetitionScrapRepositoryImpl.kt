package com.fone.competition.infrastructure

import com.fone.competition.domain.entity.Competition
import com.fone.competition.domain.entity.CompetitionScrap
import com.fone.competition.domain.repository.CompetitionScrapRepository
import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.querydsl.from.fetch
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
            fetch(CompetitionScrap::competition)
            where(
                and(
                    col(CompetitionScrap::userId).equal(userId),
                    col(Competition::id).equal(competitionId),
                )
            )
        }
    }

    override suspend fun findByUserId(userId: Long): Map<Long, CompetitionScrap?> {

        return queryFactory
            .listQuery {
                select(entity(CompetitionScrap::class))
                from(entity(CompetitionScrap::class))
                fetch(CompetitionScrap::competition)
                where(col(CompetitionScrap::userId).equal(userId))
            }
            .associateBy { it!!.competition?.id!! }
    }

    override suspend fun delete(competitionScrap: CompetitionScrap): Int {
        return queryFactory.deleteQuery<CompetitionScrap> {
            where(col(CompetitionScrap::id).equal(competitionScrap.id))
        }
    }

    override suspend fun save(competitionScrap: CompetitionScrap): CompetitionScrap {
        return competitionScrap.also {
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
