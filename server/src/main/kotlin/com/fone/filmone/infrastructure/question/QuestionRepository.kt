package com.fone.filmone.infrastructure.question

import com.fone.filmone.domain.question.entity.Question
import org.springframework.data.repository.kotlin.CoroutineCrudRepository

interface QuestionRepository : CoroutineCrudRepository<Question, Long>