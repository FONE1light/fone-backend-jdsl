package com.fone.question.infrastructure

import com.fone.question.domain.entity.Question
import com.fone.question.domain.repository.QuestionRepository
import com.linecorp.kotlinjdsl.spring.data.reactive.query.SpringDataHibernateMutinyReactiveQueryFactory
import io.smallrye.mutiny.coroutines.awaitSuspending
import org.springframework.stereotype.Repository

@Repository
class QuestionRepositoryImpl(
    private val queryFactory: SpringDataHibernateMutinyReactiveQueryFactory,
) : QuestionRepository {

    override suspend fun save(question: Question): Question {
        return question.also {
            queryFactory.withFactory { session, _ ->
                if (it.id == null) {
                    session.persist(it)
                } else {
                    session.merge(it)
                }.flatMap { session.flush() }.awaitSuspending()
            }
        }
    }
}
