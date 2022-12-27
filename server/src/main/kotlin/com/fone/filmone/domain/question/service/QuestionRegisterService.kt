package com.fone.filmone.domain.question.service

import com.fone.filmone.infrastructure.question.QuestionRepository
import com.fone.filmone.presentation.question.QuestionRegisterDto.QuestionRegisterRequest
import com.fone.filmone.presentation.question.QuestionRegisterDto.QuestionRegisterResponse
import org.springframework.stereotype.Service

@Service
class QuestionRegisterService(
    private val questionRepository: QuestionRepository
) {

    suspend fun registerQuestion(request: QuestionRegisterRequest): QuestionRegisterResponse {
        with(request) {
            val question = toEntity()
            questionRepository.save(question)

            return QuestionRegisterResponse(question)
        }
    }
}