package com.fone.filmone.domain.question.service

import com.fone.filmone.domain.question.repository.QuestionRepository
import com.fone.filmone.presentation.question.RegisterQuestionDto.RegisterQuestionRequest
import com.fone.filmone.presentation.question.RegisterQuestionDto.RegisterQuestionResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RegisterQuestionService(
    private val questionRepository: QuestionRepository
) {

    @Transactional
    suspend fun registerQuestion(request: RegisterQuestionRequest): RegisterQuestionResponse {
        with(request) {
            val question = toEntity()
            questionRepository.save(question)

            return RegisterQuestionResponse(question)
        }
    }
}