package com.fone.question.infrastructure

import com.fone.common.IntegrationTest
import com.fone.question.domain.entity.Question
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

@IntegrationTest
class QuestionRepositoryImplTest(
    private val questionRepositoryImpl: QuestionRepositoryImpl,
) : DescribeSpec({

    describe("save") {
        it("수정 테스트 성공") {
            val question = Question()
            questionRepositoryImpl.save(question)
            question.email = "test5@test.com"
            val result = questionRepositoryImpl.save(question)

            result.email shouldBe "test5@test.com"
        }

        it("저장 테스트 성공") {
            val question = Question()
            val result = questionRepositoryImpl.save(question)

            result shouldBe question
        }
    }
})
