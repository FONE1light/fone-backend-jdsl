package com.fone.question.application

import com.fone.question.domain.service.QuestionDiscordWebhookService
import com.fone.question.domain.service.RegisterQuestionService
import com.fone.question.presentation.dto.RegisterQuestionDto.RegisterQuestionRequest
import com.fone.question.presentation.dto.RegisterQuestionDto.RegisterQuestionResponse
import org.springframework.stereotype.Service

@Service
class RegisterQuestionFacade(
    private val registerQuestionService: RegisterQuestionService,
    private val discordWebhookService: QuestionDiscordWebhookService,
) {
    suspend fun registerQuestion(request: RegisterQuestionRequest): RegisterQuestionResponse {
        val response = registerQuestionService.registerQuestion(request)
        discordWebhookService.send(request.toEntity())
        return response
    }

    suspend fun registerQuestion(email: String, request: RegisterQuestionRequest): RegisterQuestionResponse {
        val response = registerQuestionService.registerQuestion(email, request)
        discordWebhookService.send(request.toEntity())
        return response
    }
}
