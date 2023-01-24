package com.fone.filmone.infrastructure.user

import com.fone.filmone.domain.user.entity.User
import com.fone.filmone.domain.user.enum.SocialLoginType
import com.fone.filmone.domain.user.repository.UserRepository
import com.linecorp.kotlinjdsl.querydsl.expression.col
import com.linecorp.kotlinjdsl.spring.data.reactive.query.SpringDataHibernateMutinyReactiveQueryFactory
import com.linecorp.kotlinjdsl.spring.data.reactive.query.singleQueryOrNull
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
                    col(User::email).equal(email),
                    col(User::socialLoginType).equal(socialLoginType),
                )
            )
        }
    }

    override suspend fun findByNickname(nickname: String): User? {
        return queryFactory.singleQueryOrNull {
            select(entity(User::class))
            from(entity(User::class))
            where(col(User::nickname).equal(nickname))
        }
    }

    override suspend fun findByNicknameOrEmail(nickname: String, email: String): User? {
        return queryFactory.singleQueryOrNull {
            select(entity(User::class))
            from(entity(User::class))
            where(
                and(
                    col(User::email).equal(email),
                    col(User::nickname).equal(nickname),
                )
            )
        }
    }

    override suspend fun findByEmail(email: String): User? {
        return queryFactory.singleQueryOrNull {
            select(entity(User::class))
            from(entity(User::class))
            where(col(User::email).equal(email))
        }
    }

    override suspend fun save(newUser: User): User {
        return newUser.also {
            queryFactory.withFactory { session, factory ->
                if (newUser.id == null) {
                    session.persist(it)
                } else {
                    session.merge(it)
                }
                    .flatMap { session.flush() }
                    .awaitSuspending()
            }
        }
    }
}