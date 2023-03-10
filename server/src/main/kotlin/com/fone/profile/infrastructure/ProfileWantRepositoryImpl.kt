package com.fone.profile.infrastructure

import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.spring.data.reactive.query.SpringDataHibernateMutinyReactiveQueryFactory
import com.linecorp.kotlinjdsl.spring.data.reactive.query.deleteQuery
import com.linecorp.kotlinjdsl.spring.data.reactive.query.listQuery
import com.linecorp.kotlinjdsl.spring.data.reactive.query.singleQueryOrNull
import com.linecorp.kotlinjdsl.spring.reactive.querydsl.SpringDataReactiveCriteriaQueryDsl
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.hibernate.reactive.mutiny.Mutiny
import org.springframework.stereotype.Repository

@Repository
class ProfileWantRepositoryImpl(
    private val sessionFactory: Mutiny.SessionFactory,
    private val queryFactory: SpringDataHibernateMutinyReactiveQueryFactory,
) : com.fone.profile.domain.repository.ProfileWantRepository {

    override suspend fun findByUserIdAndProfileId(
        userId: Long,
        profileId: Long,
    ): com.fone.profile.domain.entity.ProfileWant? {
        return queryFactory.singleQueryOrNull {
            select(entity(com.fone.profile.domain.entity.ProfileWant::class))
            from(entity(com.fone.profile.domain.entity.ProfileWant::class))
            where(
                and(
                    userIdEq(userId),
                    profileIdEq(profileId)
                )
            )
        }
    }

    override suspend fun findByUserId(userId: Long): Map<Long, com.fone.profile.domain.entity.ProfileWant?> {
        return queryFactory.listQuery {
            select(entity(com.fone.profile.domain.entity.ProfileWant::class))
            from(entity(com.fone.profile.domain.entity.ProfileWant::class))
            where(col(com.fone.profile.domain.entity.ProfileWant::userId).equal(userId))
        }.associateBy { it!!.profileId }
    }

    override suspend fun delete(profileWant: com.fone.profile.domain.entity.ProfileWant): Int {
        return queryFactory.deleteQuery<com.fone.profile.domain.entity.ProfileWant> {
            where(col(com.fone.profile.domain.entity.ProfileWant::id).equal(profileWant.id))
        }
    }

    override suspend fun save(profileWant: com.fone.profile.domain.entity.ProfileWant): com.fone.profile.domain.entity.ProfileWant {
        return profileWant.also {
            queryFactory.withFactory { session, _ ->
                if (it.id == null) {
                    session.persist(it)
                } else {
                    session.merge(it)
                }.flatMap { session.flush() }.awaitSuspending()
            }
        }
    }

    private fun SpringDataReactiveCriteriaQueryDsl<com.fone.profile.domain.entity.ProfileWant?>.userIdEq(
        userId: Long,
    ) = col(com.fone.profile.domain.entity.ProfileWant::userId).equal(userId)

    private fun SpringDataReactiveCriteriaQueryDsl<com.fone.profile.domain.entity.ProfileWant?>.profileIdEq(
        profileId: Long,
    ) = col(com.fone.profile.domain.entity.ProfileWant::profileId).equal(profileId)
}
