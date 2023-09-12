package com.fone.question.application

import com.fone.question.domain.service.RegisterQuestionService
import com.fone.question.presentation.dto.RegisterQuestionDto.RegisterQuestionRequest
import com.fone.question.presentation.dto.RegisterQuestionDto.RegisterQuestionResponse
import org.springframework.stereotype.Service

@Service
class RegisterQuestionFacade(
    private val registerQuestionService: RegisterQuestionService,
) {
    suspend fun registerQuestion(email: String? = null, request: RegisterQuestionRequest): RegisterQuestionResponse {
        val response = if (email == null) {
            registerQuestionService.registerQuestion(request)
        } else {
            registerQuestionService.registerQuestion(email, request)
        }
        return response
    }
}
