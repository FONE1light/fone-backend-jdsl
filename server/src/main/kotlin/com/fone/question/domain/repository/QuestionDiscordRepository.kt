package com.fone.question.domain.repository

import com.fone.question.domain.entity.Question

interface QuestionDiscordRepository {
    suspend fun send(question: Question)
}
