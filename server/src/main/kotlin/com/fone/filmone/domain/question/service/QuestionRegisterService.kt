package com.fone.filmone.domain.question.service

import com.fone.filmone.infrastructure.question.QuestionRepository
import com.fone.filmone.presentation.question.RegisterQuestionDto.RegisterQuestionRequest
import com.fone.filmone.presentation.question.RegisterQuestionDto.RegisterQuestionResponse
import org.springframework.stereotype.Service

@Service
class QuestionRegisterService(
    private val questionRepository: QuestionRepository
) {

    suspend fun registerQuestion(request: RegisterQuestionRequest): RegisterQuestionResponse {
        with(request) {
            val question = toEntity()
            questionRepository.save(question)

            return RegisterQuestionResponse(question)
        }
    }
}