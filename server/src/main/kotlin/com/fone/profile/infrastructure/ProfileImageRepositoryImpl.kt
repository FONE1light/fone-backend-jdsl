package com.fone.profile.infrastructure

import com.fone.profile.domain.entity.ProfileImage
import com.fone.profile.domain.repository.ProfileImageRepository
import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.spring.data.reactive.query.SpringDataHibernateMutinyReactiveQueryFactory
import com.linecorp.kotlinjdsl.spring.data.reactive.query.deleteQuery
import com.linecorp.kotlinjdsl.spring.reactive.listQuery
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.springframework.stereotype.Repository

@Repository
class ProfileImageRepositoryImpl(
    private val queryFactory: SpringDataHibernateMutinyReactiveQueryFactory,
) : ProfileImageRepository {
    override suspend fun saveAll(images: List<ProfileImage>): List<ProfileImage> {
        return queryFactory.withFactory { session, _ ->
            images.onEach {
                if (it.id == null) {
                    session.persist(it).awaitSuspending()
                } else {
                    session.merge(it).awaitSuspending()
                }
            }
        }
    }

    override suspend fun findAll(profileId: Long): List<ProfileImage> {
        return queryFactory.withFactory { factory ->
            factory.listQuery {
                select(entity(ProfileImage::class))
                from(entity(ProfileImage::class))
                where(col(ProfileImage::profileId).equal(profileId))
            }
        }
    }

    override suspend fun deleteByProfileId(profileId: Long): Int {
        return queryFactory.deleteQuery<ProfileImage> {
            where(col(ProfileImage::profileId).equal(profileId))
        }
    }
}
