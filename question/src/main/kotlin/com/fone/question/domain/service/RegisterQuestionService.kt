package com.fone.question.domain.service

import com.fone.question.domain.repository.QuestionRepository
import com.fone.question.presentation.dto.RegisterQuestionDto.RegisterQuestionRequest
import com.fone.question.presentation.dto.RegisterQuestionDto.RegisterQuestionResponse
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
