package com.fone.user.infrastructure

import com.fone.user.domain.entity.User
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
    ): User? {
        return queryFactory.singleQueryOrNull {
            select(entity(User::class))
            from(entity(User::class))
            where(
                and(
                    emailEq(email),
                    socialLoginTypeEq(socialLoginType)
                )
            )
        }
    }

    override suspend fun findByNicknameOrEmail(nickname: String?, email: String?): User? {
        return queryFactory.singleQueryOrNull {
            select(entity(User::class))
            from(entity(User::class))
            where(
                or(
                    emailEq(email),
                    nicknameEq(nickname)
                )
            )
        }
    }

    override suspend fun save(newUser: User): User {
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

    private fun SpringDataReactiveCriteriaQueryDsl<User?>.socialLoginTypeEq(
        socialLoginType: SocialLoginType?,
    ): EqualValueSpec<SocialLoginType>? {
        socialLoginType ?: return null

        return col(User::socialLoginType).equal(socialLoginType)
    }

    private fun SpringDataReactiveCriteriaQueryDsl<User?>.emailEq(
        email: String?,
    ): EqualValueSpec<String>? {
        email ?: return null

        return col(User::email).equal(email)
    }

    private fun SpringDataReactiveCriteriaQueryDsl<User?>.nicknameEq(
        nickname: String?,
    ): EqualValueSpec<String>? {
        nickname ?: return null

        return col(User::nickname).equal(nickname)
    }
}
