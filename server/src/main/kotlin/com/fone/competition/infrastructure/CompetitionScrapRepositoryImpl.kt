package com.fone.competition.infrastructure

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
) : com.fone.competition.domain.repository.CompetitionScrapRepository {

    override suspend fun findByUserIdAndCompetitionId(
        userId: Long,
        competitionId: Long,
    ): com.fone.competition.domain.entity.CompetitionScrap? {
        return queryFactory.singleQueryOrNull {
            select(entity(com.fone.competition.domain.entity.CompetitionScrap::class))
            from(entity(com.fone.competition.domain.entity.CompetitionScrap::class))
            fetch(com.fone.competition.domain.entity.CompetitionScrap::competition)
            where(
                and(
                    col(com.fone.competition.domain.entity.CompetitionScrap::userId).equal(userId),
                    col(com.fone.competition.domain.entity.Competition::id).equal(competitionId)
                )
            )
        }
    }

    override suspend fun findByUserId(userId: Long): Map<Long, com.fone.competition.domain.entity.CompetitionScrap?> {
        return queryFactory
            .listQuery {
                select(entity(com.fone.competition.domain.entity.CompetitionScrap::class))
                from(entity(com.fone.competition.domain.entity.CompetitionScrap::class))
                fetch(com.fone.competition.domain.entity.CompetitionScrap::competition)
                where(col(com.fone.competition.domain.entity.CompetitionScrap::userId).equal(userId))
            }
            .associateBy { it!!.competition?.id!! }
    }

    override suspend fun delete(competitionScrap: com.fone.competition.domain.entity.CompetitionScrap): Int {
        return queryFactory.deleteQuery<com.fone.competition.domain.entity.CompetitionScrap> {
            where(col(com.fone.competition.domain.entity.CompetitionScrap::id).equal(competitionScrap.id))
        }
    }

    override suspend fun save(competitionScrap: com.fone.competition.domain.entity.CompetitionScrap): com.fone.competition.domain.entity.CompetitionScrap {
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
