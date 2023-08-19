package com.fone.common.jwt

import io.jsonwebtoken.Claims
import io.jsonwebtoken.JwtParser
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.util.Date

@Component
class JWTUtils(
    @Value("\${security.jwt.secret}") private val secret: String,
    @Value("\${security.jwt.access-token-validity-in-seconds}") val accessTokenValidityInSeconds: Long,
    @Value("\${security.jwt.refresh-token-validity-in-seconds}") val refreshTokenValidityInSeconds: Long,
) : InitializingBean {
    private val accessTokenValidityInMilliseconds: Long
    val refreshTokenValidityInMilliseconds: Long
    private var key: Key? = null
    lateinit var jwtParser: JwtParser

    init {
        accessTokenValidityInMilliseconds = accessTokenValidityInSeconds * 1000
        refreshTokenValidityInMilliseconds = refreshTokenValidityInSeconds * 1000
    }

    override fun afterPropertiesSet() {
        val keyBytes = Decoders.BASE64.decode(secret)
        key = Keys.hmacShaKeyFor(keyBytes)
        jwtParser = Jwts.parser().setSigningKey(key).setAllowedClockSkewSeconds(Long.MAX_VALUE / 1000)
    }

    fun reissueAccessToken(refreshToken: String): Token {
        val claims = getAllClaimsFromToken(refreshToken)
        val email = getEmailFromToken(refreshToken)
        val createdDate = Date()
        return Token(
            generateAccessToken(claims, email, createdDate),
            refreshToken,
            "Bearer",
            accessTokenValidityInMilliseconds,
            createdDate
        )
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
        return jwtParser.parseClaimsJws(token).body
    }

    private fun isTokenExpired(token: String): Boolean {
        val expiration = getExpirationDateFromToken(token)
        return expiration.before(Date())
    }

    private fun getExpirationDateFromToken(token: String?): Date {
        return getAllClaimsFromToken(token).expiration
    }

    private fun doGenerateToken(claims: Map<String, Any?>, email: String): Token {
        val createdDate = Date()
        return Token(
            generateAccessToken(claims, email, createdDate),
            generateRefreshToken(claims, email, createdDate),
            "Bearer",
            accessTokenValidityInMilliseconds,
            createdDate
        )
    }

    private fun generateRefreshToken(claims: Map<String, Any?>, email: String, creationDate: Date): String {
        val accessTokenExpirationDate = Date(creationDate.time + accessTokenValidityInMilliseconds)
        return generateJWT(claims, email, creationDate, accessTokenExpirationDate)
    }
    private fun generateAccessToken(claims: Map<String, Any?>, email: String, creationDate: Date): String {
        val refreshTokenExpirationDate = Date(creationDate.time + refreshTokenValidityInMilliseconds)
        return generateJWT(claims, email, creationDate, refreshTokenExpirationDate)
    }
    private fun generateJWT(
        claims: Map<String, Any?>,
        email: String,
        creationDate: Date,
        expirationDate: Date,
    ): String {
        return Jwts.builder().setClaims(claims).setSubject(email).setIssuedAt(creationDate)
            .setExpiration(expirationDate).signWith(key).compact()
    }
}
