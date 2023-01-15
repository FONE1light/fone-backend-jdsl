package com.fone.filmone.application.question

import com.fone.filmone.domain.question.service.RegisterQuestionService
import com.fone.filmone.presentation.question.RegisterQuestionDto.RegisterQuestionRequest
import org.springframework.stereotype.Service

@Service
class RegisterQuestionFacade(
    private val registerQuestionService: RegisterQuestionService
) {
    suspend fun registerQuestion(request: RegisterQuestionRequest) =
        registerQuestionService.registerQuestion(request)
}