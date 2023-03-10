package com.fone.question.domain.repository

import com.fone.question.domain.entity.Question

interface QuestionRepository {
    suspend fun save(question: Question): Question
}
