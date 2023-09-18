package com.fone.user.infrastructure

import com.auth0.jwk.JwkProviderBuilder
import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.auth0.jwt.exceptions.JWTDecodeException
import com.auth0.jwt.exceptions.SignatureVerificationException
import com.auth0.jwt.interfaces.DecodedJWT
import com.auth0.jwt.interfaces.RSAKeyProvider
import com.fone.common.annotation.NotInUse
import com.fone.common.exception.InvalidOauthStatusException
import com.fone.common.exception.InvalidTokenException
import com.fone.user.domain.entity.OauthPrincipal
import com.fone.user.domain.enum.LoginType
import com.fone.user.domain.repository.OauthRepository
import org.springframework.stereotype.Repository
import java.net.URL
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.util.concurrent.TimeUnit

@Repository
class GoogleOauthRepository : OauthRepository {
    override val type = LoginType.GOOGLE
    private val publicKeyUrl = URL("https://www.googleapis.com/oauth2/v3/certs")
    private val jwkProvider = JwkProviderBuilder(publicKeyUrl) // public key는 수시로 변동 가능성 있다고 명시되어 있음
        .cached(20, 2, TimeUnit.HOURS)
        .build()
    private val algorithm = Algorithm.RSA256(
        object : RSAKeyProvider {
            override fun getPublicKeyById(keyId: String?): RSAPublicKey { // public key 검증만 진행하기 때문에 해당 값만 제공
                return jwkProvider.get(keyId).publicKey as RSAPublicKey
            }

            override fun getPrivateKey(): RSAPrivateKey? {
                return null
            }

            override fun getPrivateKeyId(): String? {
                return null
            }
        }
    )

    @NotInUse
    override suspend fun fetchAccessToken(code: String, state: String?): String {
        throw NotImplementedError("해당 인증 플로우 사용 안함")
    }

    @Suppress("NAME_SHADOWING", "UNCHECKED_CAST")
    override suspend fun fetchPrincipal(token: String): OauthPrincipal {
        val decoded = decode(token)
        if (decoded.issuer != "https://accounts.google.com") {
            throw InvalidTokenException()
        }
        verify(decoded) // 검증 진행. 실패 시 Exception
        when (decoded.getClaim("email_verified").asBoolean()) {
            true -> {}
            false -> throw InvalidOauthStatusException("Google에 이메일 인증되지 않은 유저")
            null -> throw InvalidOauthStatusException("Google 이메일 인증 여부 확인 불가. Oauth 권한 설정 확인 바람.")
        }
        return when (decoded.getClaim("email").asString()) {
            null -> throw InvalidOauthStatusException("Google 인증에 알 수 없는 오류")
            else -> OauthPrincipal(type, decoded.getClaim("email").asString())
        }
    }

    private fun decode(token: String): DecodedJWT {
        try {
            return JWT.decode(token)
        } catch (e: JWTDecodeException) {
            throw InvalidTokenException()
        }
    }

    private fun verify(jwt: DecodedJWT) {
        try {
            algorithm.verify(jwt)
        } catch (e: SignatureVerificationException) {
            throw InvalidTokenException()
        }
    }
}
