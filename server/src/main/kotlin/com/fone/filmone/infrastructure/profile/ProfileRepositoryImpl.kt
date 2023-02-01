package com.fone.filmone.infrastructure.profile

import com.fone.filmone.domain.common.Type
import com.fone.filmone.domain.profile.entity.Profile
import com.fone.filmone.domain.profile.entity.ProfileWant
import com.fone.filmone.domain.profile.repository.ProfileRepository
import com.linecorp.kotlinjdsl.query.spec.predicate.EqualValueSpec
import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.querydsl.expression.column
import com.linecorp.kotlinjdsl.querydsl.from.fetch
import com.linecorp.kotlinjdsl.spring.data.reactive.query.SpringDataHibernateMutinyReactiveQueryFactory
import com.linecorp.kotlinjdsl.spring.data.reactive.query.listQuery
import com.linecorp.kotlinjdsl.spring.data.reactive.query.pageQuery
import com.linecorp.kotlinjdsl.spring.data.reactive.query.singleQueryOrNull
import com.linecorp.kotlinjdsl.spring.reactive.querydsl.SpringDataReactiveCriteriaQueryDsl
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.hibernate.reactive.mutiny.Mutiny
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Slice
import org.springframework.stereotype.Repository
import javax.persistence.criteria.JoinType

@Repository
class ProfileRepositoryImpl(
    private val sessionFactory: Mutiny.SessionFactory,
    private val queryFactory: SpringDataHibernateMutinyReactiveQueryFactory,
) : ProfileRepository {

    override suspend fun findAllByType(pageable: Pageable, type: Type): Slice<Profile> {
        val ids = queryFactory.pageQuery(pageable) {
            select(column(Profile::id))
            from(entity(Profile::class))
        }.content

        val profiles = queryFactory.listQuery {
            select(entity(Profile::class))
            from(entity(Profile::class))
            fetch(Profile::profileImages, joinType = JoinType.LEFT)
            where(
                and(
                    typeEq(type),
                    col(Profile::id).`in`(ids)
                )
            )
        }

        return PageImpl(
            profiles,
            pageable,
            profiles.size.toLong(),
        )
    }

    override suspend fun findByTypeAndId(type: Type?, profileId: Long?): Profile? {
        return queryFactory.singleQueryOrNull {
            select(entity(Profile::class))
            from(entity(Profile::class))
            fetch(Profile::profileImages, joinType = JoinType.LEFT)
            where(
                and(
                    typeEq(type),
                    idEq(profileId)
                )
            )
        }
    }

    override suspend fun findAllByUserId(pageable: Pageable, userId: Long): Slice<Profile> {
        val ids = queryFactory.pageQuery(pageable) {
            select(column(Profile::id))
            from(entity(Profile::class))
        }.content

        val profiles = queryFactory.listQuery {
            select(entity(Profile::class))
            from(entity(Profile::class))
            fetch(Profile::profileImages, joinType = JoinType.LEFT)
            where(
                and(
                    col(Profile::userId).equal(userId),
                    col(Profile::id).`in`(ids)
                )
            )
        }

        return PageImpl(
            profiles,
            pageable,
            profiles.size.toLong(),
        )
    }

    override suspend fun findWantAllByUserId(
        pageable: Pageable,
        userId: Long,
        type: Type,
    ): Slice<Profile> {
        val ids = queryFactory.pageQuery(pageable) {
            select(column(ProfileWant::profileId))
            from(entity(ProfileWant::class))
            where(col(ProfileWant::userId).equal(userId))
        }.content

        val profiles = queryFactory.listQuery {
            select(entity(Profile::class))
            from(entity(Profile::class))
            fetch(Profile::profileImages, joinType = JoinType.LEFT)
            where(
                and(
                    col(Profile::id).`in`(ids)
                )
            )
        }

        return PageImpl(
            profiles,
            pageable,
            profiles.size.toLong(),
        )
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

    private fun SpringDataReactiveCriteriaQueryDsl<Profile?>.typeEq(
        type: Type?,
    ): EqualValueSpec<Type>? {
        type ?: return null

        return col(Profile::type).equal(type)
    }
}
