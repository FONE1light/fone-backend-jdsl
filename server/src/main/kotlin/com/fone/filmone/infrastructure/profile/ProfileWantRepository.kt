package com.fone.filmone.infrastructure.profile

import com.fone.filmone.domain.profile.entity.ProfileWant
import com.fone.filmone.domain.profile.repository.ProfileWantRepository
import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.spring.data.reactive.query.SpringDataHibernateMutinyReactiveQueryFactory
import com.linecorp.kotlinjdsl.spring.data.reactive.query.deleteQuery
import com.linecorp.kotlinjdsl.spring.data.reactive.query.listQuery
import com.linecorp.kotlinjdsl.spring.data.reactive.query.singleQueryOrNull
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.hibernate.reactive.mutiny.Mutiny
import org.springframework.stereotype.Repository

@Repository
class ProfileWantRepositoryImpl(
    private val sessionFactory: Mutiny.SessionFactory,
    private val queryFactory: SpringDataHibernateMutinyReactiveQueryFactory,
) : ProfileWantRepository {

    override suspend fun findByUserId(userId: Long): List<ProfileWant> {
        return queryFactory.listQuery {
            select(entity(ProfileWant::class))
            from(entity(ProfileWant::class))
            where(col(ProfileWant::userId).equal(userId))
        }
    }

    override suspend fun findByUserIdAndProfileId(userId: Long, profileId: Long): ProfileWant? {
        return queryFactory.singleQueryOrNull {
            select(entity(ProfileWant::class))
            from(entity(ProfileWant::class))
            where(
                and(
                    col(ProfileWant::userId).equal(userId),
                    col(ProfileWant::profileId).equal(profileId)
                )
            )
        }
    }

    override suspend fun delete(profileWant: ProfileWant): Int {
        return queryFactory.deleteQuery<ProfileWant> {
            where(col(ProfileWant::id).equal(profileWant.id))
        }
    }

    override suspend fun save(profileWant: ProfileWant): ProfileWant {
        return profileWant.also {
            sessionFactory.withSession { session ->
                session.persist(it).flatMap { session.flush() }
            }.awaitSuspending()
        }
    }
}