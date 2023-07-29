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
class GoogleOauthRepository(private val webClient: WebClient) : OauthRepository {
    override val type = LoginType.GOOGLE

    @NotInUse
    override suspend fun fetchAccessToken(code: String, state: String?): String {
        throw NotImplementedError("해당 인증 플로우 사용 안함")
    }

    @Suppress("NAME_SHADOWING", "UNCHECKED_CAST")
    override suspend fun fetchPrincipal(accessToken: String): OauthPrincipal {
        val accessToken = if (!accessToken.startsWith("Bearer")) "Bearer $accessToken" else accessToken
        val response =
            webClient.get().uri("https://www.googleapis.com/userinfo/v2/me").header("Authorization", accessToken)
                .exchangeToMono { response ->
                    if (response.statusCode().value() != 200) {
                        response.bodyToMono(String::class.java).flatMap {
                            Mono.error(InvalidOauthStatusException("Google Bearer Token failed! Message: $it"))
                        }
                    } else {
                        response.bodyToMono<Map<String, Any>>()
                    }
                }.awaitSingle() as Map<String, Any>
        when (response["verified_email"] as Boolean?) {
            true -> {}
            false -> throw InvalidOauthStatusException("Google에 이메일 인증되지 않은 유저")
            null -> throw InvalidOauthStatusException("Google 인증에 알 수 없는 오류")
        }
        return when (response["email"] as String?) {
            null -> throw InvalidOauthStatusException("Google 인증에 알 수 없는 오류")
            else -> OauthPrincipal(type, response["email"] as String)
        }
    }
}
