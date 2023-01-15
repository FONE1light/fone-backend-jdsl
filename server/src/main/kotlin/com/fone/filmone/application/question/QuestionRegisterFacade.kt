package com.fone.filmone.application.question

import com.fone.filmone.domain.question.service.QuestionRegisterService
import com.fone.filmone.presentation.question.RegisterQuestionDto.RegisterQuestionRequest
import org.springframework.stereotype.Service

@Service
class QuestionRegisterFacade(
    private val questionRegisterService: QuestionRegisterService
) {
    suspend fun registerQuestion(request: RegisterQuestionRequest) =
        questionRegisterService.registerQuestion(request)
}