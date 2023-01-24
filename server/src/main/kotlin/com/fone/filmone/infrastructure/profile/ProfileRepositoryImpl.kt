package com.fone.filmone.infrastructure.profile

import com.fone.filmone.domain.common.Type
import com.fone.filmone.domain.profile.entity.Profile
import com.fone.filmone.domain.profile.repository.ProfileRepository
import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.spring.data.reactive.query.SpringDataHibernateMutinyReactiveQueryFactory
import com.linecorp.kotlinjdsl.spring.data.reactive.query.listQuery
import com.linecorp.kotlinjdsl.spring.data.reactive.query.pageQuery
import com.linecorp.kotlinjdsl.spring.data.reactive.query.singleQueryOrNull
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.hibernate.reactive.mutiny.Mutiny
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.stereotype.Repository

@Repository
class ProfileRepositoryImpl(
    private val sessionFactory: Mutiny.SessionFactory,
    private val queryFactory: SpringDataHibernateMutinyReactiveQueryFactory,
) : ProfileRepository {

    override suspend fun findByType(pageable: Pageable, type: Type): Slice<Profile> {
        return queryFactory.pageQuery(pageable) {
            select(entity(Profile::class))
            from(entity(Profile::class))
            where(col(Profile::type).equal(type))
        }
    }

    override suspend fun findByType(type: Type): Profile? {
        return queryFactory.singleQueryOrNull {
            select(entity(Profile::class))
            from(entity(Profile::class))
            where(col(Profile::type).equal(type))
        }
    }

    override suspend fun findByUserId(userId: Long): List<Profile> {
        return queryFactory.listQuery {
            select(entity(Profile::class))
            from(entity(Profile::class))
            where(col(Profile::userId).equal(userId))
        }
    }

    override suspend fun findById(profileId: Long): Profile? {
        return queryFactory.singleQueryOrNull {
            select(entity(Profile::class))
            from(entity(Profile::class))
            where(col(Profile::id).equal(profileId))
        }
    }

    override suspend fun save(profile: Profile): Profile {
        return profile.also {
            sessionFactory.withSession { session ->
                session.persist(it).flatMap { session.flush() }
            }.awaitSuspending()
        }
    }

    override suspend fun findAllById(profileIds: List<Long>): List<Profile> {
        return queryFactory.listQuery {
            select(entity(Profile::class))
            from(entity(Profile::class))
            where(col(Profile::id).`in`(profileIds))
        }
    }
}