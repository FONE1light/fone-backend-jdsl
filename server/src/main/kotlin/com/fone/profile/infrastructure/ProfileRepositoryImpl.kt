package com.fone.profile.infrastructure

import com.fone.common.entity.Type
import com.fone.common.utils.DateTimeFormat
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
    private val queryFactory: SpringDataHibernateMutinyReactiveQueryFactory,
) : com.fone.profile.domain.repository.ProfileRepository {

    override suspend fun findAllByFilters(
        pageable: Pageable,
        request: RetrieveProfilesRequest,
    ): Slice<com.fone.profile.domain.entity.Profile> {
        val categoryProfileIds = queryFactory.listQuery {
            select(col(com.fone.profile.domain.entity.ProfileCategory::profileId))
            from(entity(com.fone.profile.domain.entity.ProfileCategory::class))
            where(col(com.fone.profile.domain.entity.ProfileCategory::type).`in`(request.categories))
        }

        if (categoryProfileIds.isEmpty()) {
            return PageImpl(
                listOf(),
                pageable,
                0
            )
        }

        val ids = queryFactory.pageQuery(pageable) {
            select(column(com.fone.profile.domain.entity.Profile::id))
            from(entity(com.fone.profile.domain.entity.Profile::class))
            where(
                and(
                    col(com.fone.profile.domain.entity.Profile::type).equal(request.type),
                    col(com.fone.profile.domain.entity.Profile::gender).`in`(request.genders),
                    col(com.fone.profile.domain.entity.Profile::birthday).lessThanOrEqualTo(
                        DateTimeFormat.calculdateLocalDate(request.ageMin)
                    ),
                    col(com.fone.profile.domain.entity.Profile::birthday).greaterThanOrEqualTo(
                        DateTimeFormat.calculdateLocalDate(request.ageMax)
                    ),
                    col(com.fone.profile.domain.entity.Profile::id).`in`(categoryProfileIds)
                )
            )
        }.content

        val profiles = queryFactory.listQuery {
            select(entity(com.fone.profile.domain.entity.Profile::class))
            from(entity(com.fone.profile.domain.entity.Profile::class))
            fetch(com.fone.profile.domain.entity.Profile::profileImages, joinType = JoinType.LEFT)
            where(and(col(com.fone.profile.domain.entity.Profile::id).`in`(ids)))
            orderBy(orderSpec(pageable.sort))
        }

        return PageImpl(
            profiles,
            pageable,
            profiles.size.toLong()
        )
    }

    override suspend fun findByTypeAndId(type: Type?, profileId: Long?): com.fone.profile.domain.entity.Profile? {
        return queryFactory.singleQueryOrNull {
            select(entity(com.fone.profile.domain.entity.Profile::class))
            from(entity(com.fone.profile.domain.entity.Profile::class))
            fetch(com.fone.profile.domain.entity.Profile::profileImages, joinType = JoinType.LEFT)
            where(and(typeEq(type), idEq(profileId)))
        }
    }

    override suspend fun findAllByUserId(
        pageable: Pageable,
        userId: Long,
    ): Slice<com.fone.profile.domain.entity.Profile> {
        val ids = queryFactory.pageQuery(pageable) {
            select(column(com.fone.profile.domain.entity.Profile::id))
            from(entity(com.fone.profile.domain.entity.Profile::class))
        }.content

        val profiles = queryFactory.listQuery {
            select(entity(com.fone.profile.domain.entity.Profile::class))
            from(entity(com.fone.profile.domain.entity.Profile::class))
            fetch(com.fone.profile.domain.entity.Profile::profileImages, joinType = JoinType.LEFT)
            where(
                and(
                    col(com.fone.profile.domain.entity.Profile::userId).equal(userId),
                    col(com.fone.profile.domain.entity.Profile::id).`in`(ids)
                )
            )
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
        type: Type,
    ): Slice<com.fone.profile.domain.entity.Profile> {
        val ids = queryFactory.pageQuery(pageable) {
            select(column(com.fone.profile.domain.entity.ProfileWant::profileId))
            from(entity(com.fone.profile.domain.entity.ProfileWant::class))
            where(col(com.fone.profile.domain.entity.ProfileWant::userId).equal(userId))
        }.content

        val profiles = queryFactory.listQuery {
            select(entity(com.fone.profile.domain.entity.Profile::class))
            from(entity(com.fone.profile.domain.entity.Profile::class))
            fetch(com.fone.profile.domain.entity.Profile::profileImages, joinType = JoinType.LEFT)
            where(and(col(com.fone.profile.domain.entity.Profile::id).`in`(ids)))
        }

        return PageImpl(
            profiles,
            pageable,
            profiles.size.toLong()
        )
    }

    override suspend fun save(profile: com.fone.profile.domain.entity.Profile): com.fone.profile.domain.entity.Profile {
        return profile.also {
            queryFactory.withFactory { session, _ ->
                if (it.id == null) {
                    session.persist(it)
                } else {
                    session.merge(it)
                }.flatMap { session.flush() }.awaitSuspending()
            }
        }
    }

    private fun SpringDataReactiveCriteriaQueryDsl<com.fone.profile.domain.entity.Profile?>.idEq(
        profileId: Long?,
    ): EqualValueSpec<Long?>? {
        profileId ?: return null

        return col(com.fone.profile.domain.entity.Profile::id).equal(profileId)
    }

    private fun SpringDataReactiveCriteriaQueryDsl<com.fone.profile.domain.entity.Profile?>.typeEq(
        type: Type?,
    ): EqualValueSpec<Type>? {
        type ?: return null

        return col(com.fone.profile.domain.entity.Profile::type).equal(type)
    }

    private fun SpringDataReactiveCriteriaQueryDsl<com.fone.profile.domain.entity.Profile?>.orderSpec(
        sort: Sort,
    ): List<OrderSpec> {
        val res = sort.map {
            val columnSpec = when (it.property) {
                "viewCount" -> col(com.fone.profile.domain.entity.Profile::viewCount)
                "createdAt" -> col(com.fone.profile.domain.entity.Profile::createdAt)
                else -> col(com.fone.profile.domain.entity.Profile::viewCount)
            }

            if (it.isAscending) {
                columnSpec.asc()
            } else {
                columnSpec.desc()
            }
        }.toList()

        return res
    }
}
