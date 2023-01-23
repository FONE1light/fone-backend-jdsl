package com.fone.filmone.infrastructure.profile

import com.fone.filmone.domain.profile.entity.ProfileImage
import com.fone.filmone.domain.profile.repository.ProfileImageRepository
import com.linecorp.kotlinjdsl.spring.data.reactive.query.SpringDataHibernateMutinyReactiveQueryFactory
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.hibernate.reactive.mutiny.Mutiny
import org.springframework.stereotype.Repository

@Repository
class ProfileImageRepositoryImpl(
    private val sessionFactory: Mutiny.SessionFactory,
    private val queryFactory: SpringDataHibernateMutinyReactiveQueryFactory,
) : ProfileImageRepository {

    override suspend fun saveAll(profileImages: List<ProfileImage>): List<ProfileImage> {
        sessionFactory.withSession { session ->
            session.persistAll(*profileImages.toTypedArray()).flatMap { session.flush() }
        }.awaitSuspending()

        return profileImages
    }
}