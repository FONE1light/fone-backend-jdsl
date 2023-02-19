package com.fone.profile.infrastructure

import com.fone.common.entity.DomainType
import com.fone.profile.domain.entity.ProfileDomain
import com.fone.profile.domain.repository.ProfileDomainRepository
import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.spring.data.reactive.query.SpringDataHibernateMutinyReactiveQueryFactory
import com.linecorp.kotlinjdsl.spring.data.reactive.query.deleteQuery
import com.linecorp.kotlinjdsl.spring.data.reactive.query.listQuery
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.hibernate.reactive.mutiny.Mutiny
import org.springframework.stereotype.Repository

@Repository
class ProfileDomainRepositoryImpl(
    private val sessionFactory: Mutiny.SessionFactory,
    private val queryFactory: SpringDataHibernateMutinyReactiveQueryFactory,
) : ProfileDomainRepository {

    override suspend fun saveAll(profileDomain: List<ProfileDomain>): List<ProfileDomain> {
        return profileDomain.also {
            sessionFactory.withSession { session ->
                session.persistAll(*it.toTypedArray()).flatMap { session.flush() }
            }.awaitSuspending()
        }
    }

    override suspend fun deleteByProfileId(profileId: Long): Int {
        return queryFactory.deleteQuery<ProfileDomain> {
            where(col(ProfileDomain::profileId).equal(profileId))
        }
    }

    override suspend fun findByProfileIds(profileIds: List<Long>):
            Map<Long, List<DomainType>> {
        return queryFactory.listQuery {
            select(entity(ProfileDomain::class))
            from(entity(ProfileDomain::class))
            where(col(ProfileDomain::profileId).`in`(profileIds))
        }.groupBy({ it!!.profileId }, { it!!.type })
    }

    override suspend fun findByProfileId(profileId: Long): List<DomainType> {
        return queryFactory.listQuery {
            select(col(ProfileDomain::type))
            from(entity(ProfileDomain::class))
            where(col(ProfileDomain::profileId).equal(profileId))
        }
    }
}