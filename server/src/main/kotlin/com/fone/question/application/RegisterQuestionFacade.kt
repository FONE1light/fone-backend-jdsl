package com.fone.question.application

import com.fone.question.domain.service.RegisterQuestionService
import com.fone.question.presentation.dto.RegisterQuestionDto.RegisterQuestionRequest
import org.springframework.stereotype.Service

@Service
class RegisterQuestionFacade(
    private val registerQuestionService: RegisterQuestionService,
) {
    suspend fun registerQuestion(request: RegisterQuestionRequest) = registerQuestionService.registerQuestion(request)
}
