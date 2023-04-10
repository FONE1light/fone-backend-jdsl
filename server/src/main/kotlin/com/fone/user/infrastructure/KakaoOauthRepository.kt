package com.fone.user.infrastructure

import com.fone.common.annotation.NotInUse
import com.fone.common.exception.InvalidOauthStatusException
import com.fone.user.domain.entity.OauthPrincipal
import com.fone.user.domain.enum.LoginType
import com.fone.user.domain.repository.OauthRepository
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.stereotype.Repository
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.bodyToMono
import reactor.core.publisher.Mono

@Repository
class KakaoOauthRepository(private val webClient: WebClient) : OauthRepository {
    override val type = LoginType.KAKAO

    @NotInUse
    override suspend fun fetchAccessToken(code: String, state: String?): String {
        throw NotImplementedError("해당 인증 플로우 사용 안함")
    }

    @Suppress("NAME_SHADOWING", "UNCHECKED_CAST")
    override suspend fun fetchPrincipal(accessToken: String): OauthPrincipal {
        val accessToken = if (!accessToken.startsWith("Bearer")) "Bearer $accessToken" else accessToken
        val response = webClient.get().uri("https://kapi.kakao.com/v2/user/me").header("Authorization", accessToken)
            .exchangeToMono { response ->
                if (response.statusCode().value() != 200) {
                    response.bodyToMono(String::class.java).flatMap {
                        Mono.error(InvalidOauthStatusException("Kakao Bearer Token failed! Message: $it"))
                    }
                } else {
                    response.bodyToMono<Map<String, Any>>()
                }
            }.awaitSingle()["kakao_account"]!! as Map<String, Any>
        when (response["is_email_valid"] as Boolean?) {
            true -> {}
            false -> throw InvalidOauthStatusException("해당 이메일이 다른 카카오 계정과 연동되어 있어 유효하지 않음")
            null -> throw InvalidOauthStatusException("카카오 인증에 알 수 없는 오류. 이메일 사용 동의 여부 확인 바람")
        }
        when (response["is_email_verified"] as Boolean?) {
            true -> {}
            false -> throw InvalidOauthStatusException("카카오에 이메일 인증되지 않은 유저")
            null -> throw InvalidOauthStatusException("카카오 인증에 알 수 없는 오류")
        }
        return when (response["email"] as String?) {
            null -> throw InvalidOauthStatusException("카카오 인증에 알 수 없는 오류")
            else -> OauthPrincipal(type, response["email"] as String)
        }
    }
}
