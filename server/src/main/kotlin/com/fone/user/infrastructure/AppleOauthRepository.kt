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

// Apple은 identity 토큰의 claim에 유저(subject)와 이메일 정보가 있음.
// 단, RSA(비대칭 인증)으로 JWT(JWS)을 구현했기 때문에 실제 토큰의 유효성을 검증할 필요가 있음
@Repository
class AppleOauthRepository : OauthRepository {
    override val type = LoginType.APPLE
    private val publicKeyUrl = URL("https://appleid.apple.com/auth/keys")
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

    override suspend fun fetchPrincipal(identityToken: String): OauthPrincipal {
        val decoded = decode(identityToken)
        if (decoded.issuer != "https://appleid.apple.com" || !decoded.audience.contains("com.fone.filmone")) {
            throw InvalidTokenException()
        }
        verify(decoded) // 검증 진행. 실패 시 Exception
        if (!decoded.getClaim("email_verified").asString().toBoolean()) {
            throw InvalidOauthStatusException("Apple에 이메일 인증되지 않은 유저")
        }
        val email = decoded.getClaim("email").asString()
            ?: throw InvalidOauthStatusException("Apple에 이메일이 없는 유저")
        return OauthPrincipal(type, email, decoded.subject)
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
