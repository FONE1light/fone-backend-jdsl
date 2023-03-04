package com.fone.profile.infrastructure

import com.fone.profile.domain.entity.ProfileWant
import com.fone.profile.domain.repository.ProfileWantRepository
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
    private val queryFactory: SpringDataHibernateMutinyReactiveQueryFactory
) : ProfileWantRepository {

    override suspend fun findByUserIdAndProfileId(userId: Long, profileId: Long): ProfileWant? {
        return queryFactory.singleQueryOrNull {
            select(entity(ProfileWant::class))
            from(entity(ProfileWant::class))
            where(
                and(
                    userIdEq(userId),
                    profileIdEq(profileId)
                )
            )
        }
    }

    override suspend fun findByUserId(userId: Long): Map<Long, ProfileWant?> {
        return queryFactory
            .listQuery {
                select(entity(ProfileWant::class))
                from(entity(ProfileWant::class))
                where(col(ProfileWant::userId).equal(userId))
            }
            .associateBy { it!!.profileId }
    }

    override suspend fun delete(profileWant: ProfileWant): Int {
        return queryFactory.deleteQuery<ProfileWant> {
            where(col(ProfileWant::id).equal(profileWant.id))
        }
    }

    override suspend fun save(profileWant: ProfileWant): ProfileWant {
        return profileWant.also {
            queryFactory.withFactory { session, _ ->
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

    private fun SpringDataReactiveCriteriaQueryDsl<ProfileWant?>.userIdEq(
        userId: Long
    ) = col(ProfileWant::userId).equal(userId)

    private fun SpringDataReactiveCriteriaQueryDsl<ProfileWant?>.profileIdEq(
        profileId: Long
    ) = col(ProfileWant::profileId).equal(profileId)
}
