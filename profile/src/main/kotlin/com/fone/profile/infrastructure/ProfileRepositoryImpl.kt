package com.fone.profile.infrastructure

import com.fone.common.entity.Type
import com.fone.common.utils.DateTimeFormat
import com.fone.profile.domain.entity.Profile
import com.fone.profile.domain.entity.ProfileCategory
import com.fone.profile.domain.entity.ProfileWant
import com.fone.profile.domain.repository.ProfileRepository
import com.fone.profile.presentation.dto.RetrieveProfilesDto.RetrieveProfilesRequest
import com.linecorp.kotlinjdsl.query.spec.OrderSpec
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
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Repository
import javax.persistence.criteria.JoinType

@Repository
class ProfileRepositoryImpl(
    private val sessionFactory: Mutiny.SessionFactory,
    private val queryFactory: SpringDataHibernateMutinyReactiveQueryFactory
) : ProfileRepository {

    override suspend fun findAllByFilters(
        pageable: Pageable,
        request: RetrieveProfilesRequest
    ): Slice<Profile> {
        val categoryProfileIds =
            queryFactory.listQuery {
                select(col(ProfileCategory::profileId))
                from(entity(ProfileCategory::class))
                where(col(ProfileCategory::type).`in`(request.categories))
            }

        if (categoryProfileIds.isEmpty()) {
            return PageImpl(
                listOf(),
                pageable,
                0
            )
        }

        val ids =
            queryFactory
                .pageQuery(pageable) {
                    select(column(Profile::id))
                    from(entity(Profile::class))
                    where(
                        and(
                            col(Profile::type).equal(request.type),
                            col(Profile::gender).`in`(request.genders),
                            col(Profile::birthday)
                                .lessThanOrEqualTo(
                                    DateTimeFormat.calculdateLocalDate(request.ageMin)
                                ),
                            col(Profile::birthday)
                                .greaterThanOrEqualTo(
                                    DateTimeFormat.calculdateLocalDate(request.ageMax)
                                ),
                            col(Profile::id).`in`(categoryProfileIds)
                        )
                    )
                }
                .content

        val profiles =
            queryFactory.listQuery {
                select(entity(Profile::class))
                from(entity(Profile::class))
                fetch(Profile::profileImages, joinType = JoinType.LEFT)
                where(and(col(Profile::id).`in`(ids)))
                orderBy(orderSpec(pageable.sort))
            }

        return PageImpl(
            profiles,
            pageable,
            profiles.size.toLong()
        )
    }

    override suspend fun findByTypeAndId(type: Type?, profileId: Long?): Profile? {
        return queryFactory.singleQueryOrNull {
            select(entity(Profile::class))
            from(entity(Profile::class))
            fetch(Profile::profileImages, joinType = JoinType.LEFT)
            where(and(typeEq(type), idEq(profileId)))
        }
    }

    override suspend fun findAllByUserId(pageable: Pageable, userId: Long): Slice<Profile> {
        val ids =
            queryFactory
                .pageQuery(pageable) {
                    select(column(Profile::id))
                    from(entity(Profile::class))
                }
                .content

        val profiles =
            queryFactory.listQuery {
                select(entity(Profile::class))
                from(entity(Profile::class))
                fetch(Profile::profileImages, joinType = JoinType.LEFT)
                where(and(col(Profile::userId).equal(userId), col(Profile::id).`in`(ids)))
            }

        return PageImpl(
            profiles,
            pageable,
            profiles.size.toLong()
        )
    }

    override suspend fun findWantAllByUserId(
        pageable: Pageable,
        userId: Long,
        type: Type
    ): Slice<Profile> {
        val ids =
            queryFactory
                .pageQuery(pageable) {
                    select(column(ProfileWant::profileId))
                    from(entity(ProfileWant::class))
                    where(col(ProfileWant::userId).equal(userId))
                }
                .content

        val profiles =
            queryFactory.listQuery {
                select(entity(Profile::class))
                from(entity(Profile::class))
                fetch(Profile::profileImages, joinType = JoinType.LEFT)
                where(and(col(Profile::id).`in`(ids)))
            }

        return PageImpl(
            profiles,
            pageable,
            profiles.size.toLong()
        )
    }

    override suspend fun save(profile: Profile): Profile {
        return profile.also {
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

    private fun SpringDataReactiveCriteriaQueryDsl<Profile?>.idEq(
        profileId: Long?
    ): EqualValueSpec<Long?>? {
        profileId ?: return null

        return col(Profile::id).equal(profileId)
    }

    private fun SpringDataReactiveCriteriaQueryDsl<Profile?>.typeEq(
        type: Type?
    ): EqualValueSpec<Type>? {
        type ?: return null

        return col(Profile::type).equal(type)
    }

    private fun SpringDataReactiveCriteriaQueryDsl<Profile?>.orderSpec(
        sort: Sort
    ): List<OrderSpec> {
        val res =
            sort
                .map {
                    val columnSpec =
                        when (it.property) {
                            "viewCount" -> col(Profile::viewCount)
                            "createdAt" -> col(Profile::createdAt)
                            else -> col(Profile::viewCount)
                        }

                    if (it.isAscending) {
                        columnSpec.asc()
                    } else {
                        columnSpec.desc()
                    }
                }
                .toList()

        return res
    }
}
