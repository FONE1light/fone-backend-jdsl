package com.fone.question.domain.service

import com.fone.common.exception.InvalidTokenException
import com.fone.question.domain.repository.QuestionRepository
import com.fone.question.presentation.dto.RegisterQuestionDto.RegisterQuestionRequest
import com.fone.question.presentation.dto.RegisterQuestionDto.RegisterQuestionResponse
import com.fone.user.domain.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class RegisterQuestionService(
    private val questionRepository: QuestionRepository,
    private val userRepository: UserRepository,
) {

    @Transactional
    suspend fun registerQuestion(request: RegisterQuestionRequest): RegisterQuestionResponse {
        with(request) {
            val question = toEntity()
            questionRepository.save(question)
            return RegisterQuestionResponse(question)
        }
    }

    @Transactional
    suspend fun registerQuestion(email: String, request: RegisterQuestionRequest): RegisterQuestionResponse {
        with(request) {
            val user = userRepository.findByNicknameOrEmail(email = email) ?: throw InvalidTokenException()
            val question = toEntity().apply {
                userId = user.id
            }
            questionRepository.save(question)
            return RegisterQuestionResponse(question)
        }
    }
}
