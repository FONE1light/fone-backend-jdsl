package com.fone.profile.infrastructure

import com.fone.common.entity.CategoryType
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
    private val queryFactory: SpringDataHibernateMutinyReactiveQueryFactory,
) : com.fone.profile.domain.repository.ProfileCategoryRepository {

    override suspend fun saveAll(profileCategory: List<com.fone.profile.domain.entity.ProfileCategory>): List<com.fone.profile.domain.entity.ProfileCategory> {
        return profileCategory.also {
            sessionFactory.withSession { session ->
                session.persistAll(*it.toTypedArray()).flatMap { session.flush() }
            }.awaitSuspending()
        }
    }

    override suspend fun deleteByProfileId(profileId: Long): Int {
        return queryFactory.deleteQuery<com.fone.profile.domain.entity.ProfileCategory> {
            where(col(com.fone.profile.domain.entity.ProfileCategory::profileId).equal(profileId))
        }
    }

    override suspend fun findByProfileIds(profileIds: List<Long>): Map<Long, List<CategoryType>> {
        return queryFactory.listQuery {
            select(entity(com.fone.profile.domain.entity.ProfileCategory::class))
            from(entity(com.fone.profile.domain.entity.ProfileCategory::class))
            where(col(com.fone.profile.domain.entity.ProfileCategory::profileId).`in`(profileIds))
        }.groupBy({ it!!.profileId }, { it!!.type })
    }

    override suspend fun findByProfileId(profileId: Long): List<CategoryType> {
        return queryFactory.listQuery {
            select(col(com.fone.profile.domain.entity.ProfileCategory::type))
            from(entity(com.fone.profile.domain.entity.ProfileCategory::class))
            where(col(com.fone.profile.domain.entity.ProfileCategory::profileId).equal(profileId))
        }
    }
}
