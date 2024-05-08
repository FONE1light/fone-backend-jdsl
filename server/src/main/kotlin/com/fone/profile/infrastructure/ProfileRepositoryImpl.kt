package com.fone.profile.infrastructure

import com.fone.common.config.jpa.inValues
import com.fone.common.entity.Type
import com.fone.common.utils.DateTimeFormat
import com.fone.profile.domain.entity.Profile
import com.fone.profile.domain.entity.ProfileCategory
import com.fone.profile.domain.entity.ProfileDomain
import com.fone.profile.domain.entity.ProfileImage
import com.fone.profile.domain.entity.ProfileSns
import com.fone.profile.domain.entity.ProfileWant
import com.fone.profile.domain.repository.ProfileRepository
import com.fone.profile.presentation.dto.RetrieveProfilesRequest
import com.linecorp.kotlinjdsl.query.spec.OrderSpec
import com.linecorp.kotlinjdsl.query.spec.predicate.EqualValueSpec
import com.linecorp.kotlinjdsl.querydsl.CriteriaQueryDsl
import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.querydsl.expression.column
import com.linecorp.kotlinjdsl.spring.data.reactive.query.SpringDataHibernateMutinyReactiveQueryFactory
import com.linecorp.kotlinjdsl.spring.reactive.SpringDataReactiveQueryFactory
import com.linecorp.kotlinjdsl.spring.reactive.listQuery
import com.linecorp.kotlinjdsl.spring.reactive.pageQuery
import com.linecorp.kotlinjdsl.spring.reactive.querydsl.SpringDataReactiveCriteriaQueryDsl
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.hibernate.reactive.mutiny.Mutiny
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Repository

@Repository
class ProfileRepositoryImpl(
    private val sessionFactory: Mutiny.SessionFactory,
    private val queryFactory: SpringDataHibernateMutinyReactiveQueryFactory,
) : ProfileRepository {
    override suspend fun findAllByFilters(
        pageable: Pageable,
        request: RetrieveProfilesRequest,
    ): Page<Profile> {
        val profiles: List<Profile> =
            queryFactory.withFactory { factory ->
                val domainProfileIds =
                    if (request.domains.isEmpty()) {
                        emptyList()
                    } else {
                        factory.listQuery {
                            select(col(ProfileDomain::profileId))
                            from(entity(ProfileDomain::class))
                            where(col(ProfileDomain::type).inValues(request.domains))
                        }
                    }

                val categoryProfileIds =
                    if (request.categories.isEmpty()) {
                        emptyList()
                    } else {
                        factory.listQuery {
                            select(col(ProfileCategory::profileId))
                            from(entity(ProfileCategory::class))
                            where(col(ProfileCategory::type).inValues(request.categories))
                        }
                    }

                val ids =
                    factory.pageQuery(pageable) {
                        select(column(Profile::id))
                        from(entity(Profile::class))
                        whereAnd(
                            col(Profile::type).equal(request.type),
                            if (request.genders.isNotEmpty()) col(Profile::gender).inValues(request.genders) else null,
                            col(Profile::birthday).lessThanOrEqualTo(
                                DateTimeFormat.calculdateLocalDate(request.ageMin)
                            ),
                            col(Profile::birthday).greaterThanOrEqualTo(
                                DateTimeFormat.calculdateLocalDate(request.ageMax)
                            ),
                            if (request.domains.isNotEmpty()) col(Profile::id).inValues(domainProfileIds) else null,
                            if (request.categories.isNotEmpty()) {
                                col(
                                    Profile::id
                                ).inValues(categoryProfileIds)
                            } else {
                                null
                            },
                            col(Profile::isDeleted).equal(false)
                        )
                    }

                if (ids.content.isEmpty()) {
                    listOf()
                } else {
                    factory.profileUrlsAndProfileImages {
                        where(and(col(Profile::id).inValues(ids.content)))
                        orderBy(orderSpec(pageable.sort))
                    }
                }
            }

        return PageImpl(
            profiles,
            pageable,
            profiles.size.toLong()
        )
    }

    override suspend fun findById(profileId: Long): Profile? {
        return queryFactory.profileUrlsAndProfileImages {
            where(idEq(profileId))
        }.firstOrNull()
    }

    override suspend fun findByTypeAndId(
        type: Type?,
        profileId: Long,
    ): Profile? {
        return findById(profileId)
    }

    override suspend fun findAllByUserId(
        pageable: Pageable,
        userId: Long,
    ): Page<Profile> {
        return queryFactory.withFactory { factory ->
            val ids =
                factory.pageQuery(pageable) {
                    select(column(Profile::id))
                    from(entity(Profile::class))
                    where(
                        and(
                            col(Profile::userId).equal(userId),
                            col(Profile::isDeleted).equal(false)
                        )
                    )
                }
            val profiles =
                factory.profileUrlsAndProfileImages {
                    where(col(Profile::id).inValues(ids.content))
                }.associateBy { it.id }

            ids.map { profiles[it] }
        }
    }

    override suspend fun findWantAllByUserId(
        pageable: Pageable,
        userId: Long,
        type: Type?,
    ): Page<Profile> {
        return queryFactory.withFactory { factory ->
            val ids =
                factory.pageQuery(pageable) {
                    select(column(ProfileWant::profileId))
                    from(entity(ProfileWant::class))
                    join(entity(Profile::class), col(Profile::id).equal(col(ProfileWant::profileId)))
                    where(
                        and(
                            col(ProfileWant::userId).equal(userId),
                            col(Profile::isDeleted).equal(false),
                            if (type != null) col(Profile::type).equal(type) else null
                        )
                    )
                }
            val profiles =
                factory.profileUrlsAndProfileImages {
                    where(col(Profile::id).inValues(ids.content))
                }.associateBy { it.id }

            PageImpl(
                ids.content.reversed().mapNotNull { profiles[it] },
                pageable,
                ids.totalElements
            )
        }
    }

    override suspend fun save(profile: Profile): Profile {
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

    private fun CriteriaQueryDsl<Profile>.idEq(profileId: Long?): EqualValueSpec<Long?>? {
        profileId ?: return null

        return col(Profile::id).equal(profileId)
    }

    private fun CriteriaQueryDsl<Profile>.orderSpec(sort: Sort): List<OrderSpec> {
        val res =
            sort.map {
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
            }.toList()

        return res
    }

    private suspend fun SpringDataReactiveQueryFactory.profileUrlsAndProfileImages(
        block: SpringDataReactiveCriteriaQueryDsl<Profile>.() -> Unit,
    ): List<Profile> {
        val profiles =
            listQuery {
                select(entity(Profile::class))
                from(entity(Profile::class))
                block()
            }
        val ids = profiles.map { it.id }
        val images =
            listQuery {
                select(entity(ProfileImage::class))
                from(entity(ProfileImage::class))
                where(col(ProfileImage::profileId).inValues(ids))
            }.groupBy { it!!.profileId } as Map<Long, List<ProfileImage>>
        val urls =
            listQuery {
                select(entity(ProfileSns::class))
                from(entity(ProfileSns::class))
                where(col(ProfileSns::profileId).inValues(ids))
            }.groupBy { it!!.profileId } as Map<Long, List<ProfileSns>>
        profiles.forEach {
            it.snsUrls = urls.getOrDefault(it.id!!, listOf()).toSet()
            it.profileImages = images.getOrDefault(it.id, listOf()).toMutableList()
        }
        return profiles
    }

    private suspend fun SpringDataHibernateMutinyReactiveQueryFactory.profileUrlsAndProfileImages(
        block: SpringDataReactiveCriteriaQueryDsl<Profile>.() -> Unit,
    ): List<Profile> {
        return withFactory { factory ->
            factory.profileUrlsAndProfileImages(block)
        }
    }
}
