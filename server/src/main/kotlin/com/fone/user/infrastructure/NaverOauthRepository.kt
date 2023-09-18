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
class NaverOauthRepository(private val webClient: WebClient) : OauthRepository {
    override val type = LoginType.NAVER

    @NotInUse
    override suspend fun fetchAccessToken(code: String, state: String?): String {
        throw NotImplementedError("해당 인증 플로우 사용 안함")
    }

    @Suppress("NAME_SHADOWING", "UNCHECKED_CAST")
    override suspend fun fetchPrincipal(token: String): OauthPrincipal {
        val accessToken = if (!token.startsWith("Bearer")) "Bearer $token" else token
        val response = webClient.get().uri("https://openapi.naver.com/v1/nid/me").header("Authorization", accessToken)
            .exchangeToMono { response ->
                if (!response.statusCode().is2xxSuccessful) {
                    response.bodyToMono(String::class.java).flatMap {
                        Mono.error(InvalidOauthStatusException("Naver Bearer Token failed! Message: $it"))
                    }
                } else {
                    response.bodyToMono<Map<String, Any>>()
                }
            }.awaitSingle()["response"]!! as Map<String, String>
        val email = response["email"] ?: throw InvalidOauthStatusException("네이버에 이메일이 없는 유저")
        return OauthPrincipal(type, email)
    }
}
