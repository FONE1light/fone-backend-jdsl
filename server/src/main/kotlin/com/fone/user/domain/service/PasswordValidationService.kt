package com.fone.user.domain.service

import com.fone.user.domain.enum.LoginType
import com.fone.user.domain.repository.UserRepository
import com.fone.user.presentation.dto.PasswordValidationRequest
import com.fone.user.presentation.dto.PasswordValidationResponse
import com.fone.user.presentation.dto.ResponseType
import org.springframework.stereotype.Service

@Service
class PasswordValidationService(
    private val userRepository: UserRepository,
) {
    suspend fun validateRequest(request: PasswordValidationRequest): PasswordValidationResponse {
        return if (doesExist(request.email)) {
            PasswordValidationResponse(ResponseType.INVALID)
        } else {
            PasswordValidationResponse(ResponseType.VALID)
        }
    }

    private suspend fun doesExist(email: String): Boolean {
        return userRepository.findByEmailAndLoginType(email, LoginType.PASSWORD) != null
    }
}
