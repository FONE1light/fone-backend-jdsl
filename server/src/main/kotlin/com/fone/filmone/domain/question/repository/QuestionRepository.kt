package com.fone.filmone.domain.question.repository

import com.fone.filmone.domain.question.entity.Question

interface QuestionRepository {
    suspend fun save(question: Question): Question
}