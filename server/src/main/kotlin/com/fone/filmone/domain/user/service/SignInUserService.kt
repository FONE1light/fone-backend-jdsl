package com.fone.filmone.domain.user.service

import com.fone.filmone.common.exception.NotFoundUserException
import com.fone.filmone.common.jwt.JWTUtils
import com.fone.filmone.domain.user.enum.Role
import com.fone.filmone.infrastructure.user.UserRepository
import com.fone.filmone.presentation.user.CheckNicknameDuplicateDto.CheckNicknameDuplicateRequest
import com.fone.filmone.presentation.user.CheckNicknameDuplicateDto.CheckNicknameDuplicateResponse
import com.fone.filmone.presentation.user.SignInUserDto.SignInUserRequest
import com.fone.filmone.presentation.user.SignInUserDto.SignInUserResponse
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SignInUserService(
    private val userRepository: UserRepository,
    val jwtUtils: JWTUtils,
) {

    @Transactional(readOnly = true)
    suspend fun signInUser(request: SignInUserRequest): SignInUserResponse {
        with(request) {
            val user = userRepository.findByEmailAndSocialLoginType(
                email,
                socialLoginType.toString()
            ) ?: throw NotFoundUserException()

            val token = jwtUtils.generateUserToken(
                user.email,
                user.roles.split(",").map { Role("ROLE_USER") })

            return SignInUserResponse(user, token)
        }
    }
}