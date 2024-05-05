package com.fone.profile.infrastructure

import com.fone.profile.domain.entity.ProfileSns
import com.fone.profile.domain.repository.ProfileSnsRepository
import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.spring.data.reactive.query.SpringDataHibernateMutinyReactiveQueryFactory
import com.linecorp.kotlinjdsl.spring.data.reactive.query.deleteQuery
import com.linecorp.kotlinjdsl.spring.reactive.listQuery
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.springframework.stereotype.Repository

@Repository
class ProfileSnsRepositoryImpl(
    private val queryFactory: SpringDataHibernateMutinyReactiveQueryFactory,
) : ProfileSnsRepository {
    override suspend fun saveAll(urls: Set<ProfileSns>): Set<ProfileSns> {
        return queryFactory.withFactory { session, _ ->
            urls.onEach {
                if (it.id == null) {
                    session.persist(it).awaitSuspending()
                } else {
                    session.merge(it).awaitSuspending()
                }
            }
        }
    }

    override suspend fun findAll(profileId: Long): Set<ProfileSns> {
        return queryFactory.withFactory { factory ->
            factory.listQuery {
                select(entity(ProfileSns::class))
                from(entity(ProfileSns::class))
                where(col(ProfileSns::profileId).equal(profileId))
            }.filterNotNull().toSet()
        }
    }

    override suspend fun deleteByProfileId(profileId: Long): Int {
        return queryFactory.deleteQuery<ProfileSns> {
            where(col(ProfileSns::profileId).equal(profileId))
        }
    }
}
