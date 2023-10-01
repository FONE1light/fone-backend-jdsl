package com.fone.user.infrastructure

import com.fone.user.domain.entity.User
import com.fone.user.domain.enum.LoginType
import com.fone.user.domain.repository.UserRepository
import com.linecorp.kotlinjdsl.query.spec.predicate.EqualValueSpec
import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.spring.data.reactive.query.SpringDataHibernateMutinyReactiveQueryFactory
import com.linecorp.kotlinjdsl.spring.data.reactive.query.listQuery
import com.linecorp.kotlinjdsl.spring.data.reactive.query.singleQueryOrNull
import com.linecorp.kotlinjdsl.spring.reactive.querydsl.SpringDataReactiveCriteriaQueryDsl
import com.linecorp.kotlinjdsl.spring.reactive.singleQueryOrNull
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(
    private val queryFactory: SpringDataHibernateMutinyReactiveQueryFactory,
) : UserRepository {

    override suspend fun findById(
        userId: Long,
    ): User? {
        return queryFactory.singleQueryOrNull {
            select(entity(User::class))
            from(entity(User::class))
            where(
                idEq(userId)
            )
        }
    }

    override suspend fun findByIds(userIds: List<Long>): List<User> {
        return queryFactory.listQuery {
            select(entity(User::class))
            from(entity(User::class))
            where(
                col(User::id).`in`(userIds)
            )
        }
    }

    override suspend fun findByEmail(
        email: String,
    ): User? {
        return queryFactory.singleQueryOrNull {
            select(entity(User::class))
            from(entity(User::class))
            where(
                emailEq(email)
            )
        }
    }

    override suspend fun findByEmailAndLoginType(
        email: String,
        loginType: LoginType,
    ): User? {
        return queryFactory.singleQueryOrNull {
            select(entity(User::class))
            from(entity(User::class))
            where(
                and(
                    emailEq(email),
                    loginTypeEq(loginType)
                )
            )
        }
    }

    override suspend fun findByIdentifier(
        identifier: String,
    ): User? {
        return queryFactory.singleQueryOrNull {
            select(entity(User::class))
            from(entity(User::class))
            where(
                col(User::identifier).equal(identifier)
            )
        }
    }

    override suspend fun findByPhone(
        phone: String,
    ): User? {
        return queryFactory.singleQueryOrNull {
            select(entity(User::class))
            from(entity(User::class))
            where(
                col(User::phoneNumber).equal(phone)
            )
        }
    }

    override suspend fun findByNicknameOrEmail(
        nickname: String?,
        email: String?,
    ): User? {
        return queryFactory.withFactory { factory ->
            factory.singleQueryOrNull {
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

    private fun SpringDataReactiveCriteriaQueryDsl<User?>.idEq(
        userId: Long,
    ): EqualValueSpec<Long?> {
        return col(User::id).equal(userId)
    }

    private fun SpringDataReactiveCriteriaQueryDsl<User?>.loginTypeEq(
        loginType: LoginType?,
    ): EqualValueSpec<LoginType>? {
        loginType ?: return null

        return col(User::loginType).equal(loginType)
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
