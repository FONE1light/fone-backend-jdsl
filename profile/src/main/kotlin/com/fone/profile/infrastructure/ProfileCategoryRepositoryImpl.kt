package com.fone.profile.infrastructure

import com.fone.common.entity.CategoryType
import com.fone.profile.domain.entity.ProfileCategory
import com.fone.profile.domain.repository.ProfileCategoryRepository
import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.spring.data.reactive.query.SpringDataHibernateMutinyReactiveQueryFactory
import com.linecorp.kotlinjdsl.spring.data.reactive.query.deleteQuery
import com.linecorp.kotlinjdsl.spring.data.reactive.query.listQuery
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.hibernate.reactive.mutiny.Mutiny
import org.springframework.stereotype.Repository

@Repository
class ProfileCategoryRepositoryImpl(
    private val sessionFactory: Mutiny.SessionFactory,
    private val queryFactory: SpringDataHibernateMutinyReactiveQueryFactory
) : ProfileCategoryRepository {

    override suspend fun saveAll(profileCategory: List<ProfileCategory>): List<ProfileCategory> {
        return profileCategory.also {
            sessionFactory
                .withSession { session ->
                    session.persistAll(*it.toTypedArray()).flatMap { session.flush() }
                }
                .awaitSuspending()
        }
    }

    override suspend fun deleteByProfileId(profileId: Long): Int {
        return queryFactory.deleteQuery<ProfileCategory> {
            where(col(ProfileCategory::profileId).equal(profileId))
        }
    }

    override suspend fun findByProfileIds(profileIds: List<Long>): Map<Long, List<CategoryType>> {
        return queryFactory
            .listQuery {
                select(entity(ProfileCategory::class))
                from(entity(ProfileCategory::class))
                where(col(ProfileCategory::profileId).`in`(profileIds))
            }
            .groupBy({ it!!.profileId }, { it!!.type })
    }

    override suspend fun findByProfileId(profileId: Long): List<CategoryType> {
        return queryFactory.listQuery {
            select(col(ProfileCategory::type))
            from(entity(ProfileCategory::class))
            where(col(ProfileCategory::profileId).equal(profileId))
        }
    }
}
