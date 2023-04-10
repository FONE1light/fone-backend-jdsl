package com.fone.user.domain.service

import com.fone.common.exception.NotFoundUserException
import com.fone.user.domain.entity.OauthPrincipal
import com.fone.user.domain.enum.LoginType
import com.fone.user.domain.repository.OauthRepository
import com.fone.user.domain.repository.UserRepository
import com.fone.user.presentation.dto.OauthEmailDto
import org.springframework.stereotype.Service

@Service
class OauthValidationService(
    private val oauthRepositories: List<OauthRepository>,
    private val userRepository: UserRepository,
) {
    suspend fun getAccessToken(authenticationCode: String, state: String?, type: LoginType): String {
        return oauthRepositories.findType(type).fetchAccessToken(authenticationCode, state)
    }

    suspend fun getEmailResponse(request: OauthEmailDto.OauthEmailRequest): OauthEmailDto.OauthEmailResponse {
        val principal = getPrincipal(request.loginType, request.accessToken)
        return when (request.loginType) {
            LoginType.APPLE -> {
                userRepository.findByIdentifier(principal.identifier!!) ?: return request.toResponse(
                    principal.email,
                    false
                )
                request.toResponse(principal.email, true)
            }

            else -> {
                val email = principal.email
                userRepository.findByEmailAndLoginType(email, request.loginType)
                    ?: return request.toResponse(email, false)
                request.toResponse(email, true)
            }
        }
    }

    // sign-in의 경우 토큰의 유효성 및 기존 User와 일치하는지 확인
    suspend fun isValidTokenSignIn(loginType: LoginType, accessToken: String, email: String): Boolean {
        return when (loginType) {
            LoginType.APPLE -> {
                val user = userRepository.findByEmailAndLoginType(email, loginType)
                    ?: throw NotFoundUserException()
                val identifier = user.identifier ?: throw IllegalStateException("APPLE 유저가 identifier가 없음!")
                identifier == oauthRepositories.findType(loginType).fetchPrincipal(accessToken).identifier
            }

            else -> email == getPrincipal(loginType, accessToken).email
        }
    }

    // Sign-up의 경우 토큰의 유효성만 검증
    suspend fun isValidTokenSignUp(
        loginType: LoginType,
        accessToken: String,
        email: String,
        identifier: String?,
    ): Boolean {
        return when (loginType) {
            LoginType.APPLE -> {
                identifier == oauthRepositories.findType(loginType).fetchPrincipal(accessToken).identifier
            }

            else -> email == getPrincipal(loginType, accessToken).email
        }
    }

    private suspend fun getPrincipal(loginType: LoginType, accessToken: String): OauthPrincipal {
        return oauthRepositories.findType(loginType)
            .fetchPrincipal(accessToken)
    }

    private fun List<OauthRepository>.findType(type: LoginType) =
        this.first { repo -> repo.type == type }
}
