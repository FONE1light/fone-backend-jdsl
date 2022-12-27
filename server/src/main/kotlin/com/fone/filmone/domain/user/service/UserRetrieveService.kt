package com.fone.filmone.domain.user.service

import com.fone.filmone.common.exception.UserNotFoundException
import com.fone.filmone.common.jwt.JWTUtils
import com.fone.filmone.infrastructure.user.UserRepository
import com.fone.filmone.presentation.auth.CheckNicknameDuplicateDto.CheckNicknameDuplicateRequest
import com.fone.filmone.presentation.auth.CheckNicknameDuplicateDto.CheckNicknameDuplicateResponse
import com.fone.filmone.presentation.auth.SignInDto.SignInRequest
import com.fone.filmone.presentation.auth.SignInDto.SignInResponse
import org.springframework.stereotype.Service

@Service
class UserRetrieveService(
    private val userRepository: UserRepository,
    val jwtUtils: JWTUtils,
) {

    suspend fun retrieveUser(request: SignInRequest): SignInResponse {
        with(request) {
            val user = userRepository.findByEmailAndSocialLoginType(
                email,
                socialLoginType.toString()
            ) ?: throw UserNotFoundException()

            val token = jwtUtils.generateUserToken(user.email)

            return SignInResponse(user, token)
        }
    }

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