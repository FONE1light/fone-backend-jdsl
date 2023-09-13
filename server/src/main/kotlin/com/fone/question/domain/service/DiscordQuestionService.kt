package com.fone.question.domain.service

import com.fone.question.domain.entity.Question
import com.fone.question.domain.repository.QuestionDiscordRepository
import org.springframework.stereotype.Service

@Service
class DiscordQuestionService(
    private val questionDiscordRepository: QuestionDiscordRepository,
) {

    suspend fun sendQuestion(question: Question) = questionDiscordRepository.send(question)
}
