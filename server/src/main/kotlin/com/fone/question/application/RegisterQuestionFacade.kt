package com.fone.question.application

import com.fone.question.domain.service.DiscordQuestionService
import com.fone.question.domain.service.RegisterQuestionService
import com.fone.question.presentation.dto.RegisterQuestionRequest
import com.fone.question.presentation.dto.RegisterQuestionResponse
import org.springframework.stereotype.Service

@Service
class RegisterQuestionFacade(
    private val registerQuestionService: RegisterQuestionService,
    private val discordQuestionService: DiscordQuestionService,
) {
    suspend fun registerQuestion(email: String? = null, request: RegisterQuestionRequest): RegisterQuestionResponse {
        val question = if (email == null) {
            registerQuestionService.registerQuestion(request)
        } else {
            registerQuestionService.registerQuestion(email, request)
        }
        discordQuestionService.sendQuestion(question)
        return RegisterQuestionResponse(question)
    }
}
