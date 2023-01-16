package com.fone.filmone.domain.user.service

import com.fone.filmone.infrastructure.user.UserRepository
import com.fone.filmone.presentation.user.CheckNicknameDuplicateDto.CheckNicknameDuplicateRequest
import com.fone.filmone.presentation.user.CheckNicknameDuplicateDto.CheckNicknameDuplicateResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CheckNicknameDuplicateService(
    private val userRepository: UserRepository,
) {

    @Transactional(readOnly = true)
    suspend fun checkNicknameDuplicate(request: CheckNicknameDuplicateRequest):
            CheckNicknameDuplicateResponse {
        with(request) {
            userRepository.findByNickname(nickname)?.let {
                return CheckNicknameDuplicateResponse(request.nickname, true)
            }

            return CheckNicknameDuplicateResponse(request.nickname, false)
        }
    }
}