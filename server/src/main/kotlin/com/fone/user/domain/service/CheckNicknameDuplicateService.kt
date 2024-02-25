package com.fone.user.domain.service

import com.fone.common.exception.DuplicateUserNicknameException
import com.fone.user.domain.repository.UserRepository
import com.fone.user.presentation.dto.CheckNicknameDuplicateRequest
import com.fone.user.presentation.dto.CheckNicknameDuplicateResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CheckNicknameDuplicateService(
    private val userRepository: UserRepository,
) {

    @Transactional(readOnly = true)
    suspend fun checkNicknameDuplicate(
        request: CheckNicknameDuplicateRequest,
    ): CheckNicknameDuplicateResponse {
        with(request) {
            userRepository.findByNicknameOrEmail(nickname, null)?.let {
                throw DuplicateUserNicknameException()
            }

            return CheckNicknameDuplicateResponse(nickname, false)
        }
    }
}
