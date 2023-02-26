package com.fone.common.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import java.security.Key
import java.util.*
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class JWTUtils(
    @Value("\${security.jwt.secret}") private val secret: String,
    @Value("\${security.jwt.access-token-validity-in-seconds}")
    val accessTokenValidityInSeconds: Long,
    @Value("\${security.jwt.refresh-token-validity-in-seconds}")
    val refreshTokenValidityInSeconds: Long,
) : InitializingBean {
    private val accessTokenValidityInMilliseconds: Long
    val refreshTokenValidityInMilliseconds: Long
    private var key: Key? = null

    init {
        accessTokenValidityInMilliseconds = accessTokenValidityInSeconds * 1000
        refreshTokenValidityInMilliseconds = refreshTokenValidityInSeconds * 1000
    }

    override fun afterPropertiesSet() {
        val keyBytes = Decoders.BASE64.decode(secret)
        key = Keys.hmacShaKeyFor(keyBytes)
    }

    fun generateUserToken(email: String, roles: List<Role>): Token {
        val claims: MutableMap<String, Any?> = HashMap()
        claims["roles"] = roles

        return doGenerateToken(claims, email)
    }

    fun getEmailFromToken(token: String?): String {
        return getAllClaimsFromToken(token).subject
    }

    fun validateToken(token: String): Boolean {
        return !isTokenExpired(token)
    }

    fun getAllClaimsFromToken(token: String?): Claims {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).body
    }

    private fun isTokenExpired(token: String): Boolean {
        val expiration = getExpirationDateFromToken(token)
        return expiration.before(Date())
    }

    private fun getExpirationDateFromToken(token: String?): Date {
        return getAllClaimsFromToken(token).expiration
    }

    private fun doGenerateToken(claims: Map<String, Any?>, email: String): Token {
        val accessTokenExpirationTimeLong = accessTokenValidityInMilliseconds
        val refreshTokenExpirationTimeLong = refreshTokenValidityInMilliseconds
        val createdDate = Date()
        val accessTokenExpirationDate =
            Date(createdDate.time + accessTokenExpirationTimeLong * 1000)
        val refreshTokenExpirationDate =
            Date(createdDate.time + refreshTokenExpirationTimeLong * 1000)

        val accessToken =
            Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(createdDate)
                .setExpiration(accessTokenExpirationDate)
                .signWith(key)
                .compact()
        val refreshToken =
            Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(createdDate)
                .setExpiration(refreshTokenExpirationDate)
                .signWith(key)
                .compact()

        return Token(
            accessToken,
            refreshToken,
            "Bearer",
            accessTokenExpirationTimeLong,
            createdDate
        )
    }
}
