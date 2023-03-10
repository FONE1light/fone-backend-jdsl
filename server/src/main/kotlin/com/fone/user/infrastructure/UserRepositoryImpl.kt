package com.fone.user.infrastructure

import com.fone.user.domain.enum.SocialLoginType
import com.fone.user.domain.repository.UserRepository
import com.linecorp.kotlinjdsl.query.spec.predicate.EqualValueSpec
import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.spring.data.reactive.query.SpringDataHibernateMutinyReactiveQueryFactory
import com.linecorp.kotlinjdsl.spring.data.reactive.query.singleQueryOrNull
import com.linecorp.kotlinjdsl.spring.reactive.querydsl.SpringDataReactiveCriteriaQueryDsl
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.hibernate.reactive.mutiny.Mutiny
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(
    private val sessionFactory: Mutiny.SessionFactory,
    private val queryFactory: SpringDataHibernateMutinyReactiveQueryFactory,
) : UserRepository {

    override suspend fun findByEmailAndSocialLoginType(
        email: String,
        socialLoginType: SocialLoginType,
    ): com.fone.user.domain.entity.User? {
        return queryFactory.singleQueryOrNull {
            select(entity(com.fone.user.domain.entity.User::class))
            from(entity(com.fone.user.domain.entity.User::class))
            where(
                and(
                    emailEq(email),
                    socialLoginTypeEq(socialLoginType)
                )
            )
        }
    }

    override suspend fun findByNicknameOrEmail(nickname: String?, email: String?): com.fone.user.domain.entity.User? {
        return queryFactory.singleQueryOrNull {
            select(entity(com.fone.user.domain.entity.User::class))
            from(entity(com.fone.user.domain.entity.User::class))
            where(
                or(
                    emailEq(email),
                    nicknameEq(nickname)
                )
            )
        }
    }

    override suspend fun save(newUser: com.fone.user.domain.entity.User): com.fone.user.domain.entity.User {
        return newUser.also {
            queryFactory.withFactory { session, _ ->
                if (it.id == null) {
                    session.persist(it)
                } else {
                    session.merge(it)
                }.flatMap { session.flush() }.awaitSuspending()
            }
        }
    }

    private fun SpringDataReactiveCriteriaQueryDsl<com.fone.user.domain.entity.User?>.socialLoginTypeEq(
        socialLoginType: SocialLoginType?,
    ): EqualValueSpec<SocialLoginType>? {
        socialLoginType ?: return null

        return col(com.fone.user.domain.entity.User::socialLoginType).equal(socialLoginType)
    }

    private fun SpringDataReactiveCriteriaQueryDsl<com.fone.user.domain.entity.User?>.emailEq(
        email: String?,
    ): EqualValueSpec<String>? {
        email ?: return null

        return col(com.fone.user.domain.entity.User::email).equal(email)
    }

    private fun SpringDataReactiveCriteriaQueryDsl<com.fone.user.domain.entity.User?>.nicknameEq(
        nickname: String?,
    ): EqualValueSpec<String>? {
        nickname ?: return null

        return col(com.fone.user.domain.entity.User::nickname).equal(nickname)
    }
}
