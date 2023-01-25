package com.fone.filmone.infrastructure.profile

import com.fone.filmone.domain.common.Type
import com.fone.filmone.domain.profile.entity.Profile
import com.fone.filmone.domain.profile.entity.ProfileWant
import com.fone.filmone.domain.profile.repository.ProfileRepository
import com.linecorp.kotlinjdsl.query.spec.predicate.EqualValueSpec
import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.querydsl.expression.column
import com.linecorp.kotlinjdsl.spring.data.reactive.query.*
import com.linecorp.kotlinjdsl.spring.reactive.querydsl.SpringDataReactiveCriteriaQueryDsl
import com.linecorp.kotlinjdsl.spring.reactive.querydsl.SpringDataReactivePageableQueryDsl
import io.smallrye.mutiny.coroutines.awaitSuspending
import kotlinx.coroutines.selects.select
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
            where(
                and(
                    typeEq(type)
                )
            )
        }
    }

    override suspend fun findByType(type: Type): Profile? {
        return queryFactory.singleQueryOrNull {
            select(entity(Profile::class))
            from(entity(Profile::class))
            where(
                and(
                    typeEq(type)
                )
            )
        }
    }

    override suspend fun findByUserId(pageable: Pageable, userId: Long): Slice<Profile> {
        return queryFactory.pageQuery(pageable) {
            select(entity(Profile::class))
            from(entity(Profile::class))
            where(
                and(
                    userIdEq(userId)
                )
            )
        }
    }

    override suspend fun findById(profileId: Long): Profile? {
        return queryFactory.singleQueryOrNull {
            select(entity(Profile::class))
            from(entity(Profile::class))
            where(
                and(
                    idEq(profileId)
                )
            )
        }
    }

    override suspend fun findAllById(
        pageable: Pageable,
        userId: Long,
        type: Type
    ): Slice<Profile> {
        val profileIds = queryFactory.subquery {
            select(column(ProfileWant::id))
            from(entity(ProfileWant::class))
            where(col(ProfileWant::userId).equal(userId))
        }

        return queryFactory.pageQuery(pageable) {
            select(entity(Profile::class))
            from(entity(Profile::class))
            where(
                and(
                    col(Profile::id).`in`(profileIds),
                    typeEq(type),
                )
            )
        }
    }

    override suspend fun save(profile: Profile): Profile {
        return profile.also {
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

    private fun SpringDataReactiveCriteriaQueryDsl<Profile?>.idEq(
        profileId: Long?,
    ): EqualValueSpec<Long?>? {
        profileId ?: return null

        return col(Profile::id).equal(profileId)
    }

    private fun SpringDataReactivePageableQueryDsl<Profile>.userIdEq(
        userId: Long?,
    ): EqualValueSpec<Long>? {
        userId ?: return null

        return col(Profile::userId).equal(userId)
    }

    private fun SpringDataReactiveCriteriaQueryDsl<Profile?>.typeEq(
        type: Type?,
    ): EqualValueSpec<Type>? {
        type ?: return null

        return col(Profile::type).equal(type)
    }

    private fun SpringDataReactivePageableQueryDsl<Profile>.typeEq(
        type: Type?,
    ): EqualValueSpec<Type>? {
        type ?: return null

        return col(Profile::type).equal(type)
    }
}
