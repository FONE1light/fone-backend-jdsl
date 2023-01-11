package com.fone.filmone.common.jwt

import com.fone.filmone.domain.user.Token
import com.fone.filmone.domain.user.enum.Role
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*
import javax.annotation.PostConstruct

@Component
class JWTUtils {
    @Value("\${security.jwt.secret}")
    private lateinit var secret: String

    @Value("\${security.jwt.access-token-validity-in-seconds}")
    private lateinit var accessTokenExpirationTime: String

    private lateinit var key: Key

    @PostConstruct
    fun init() {
        key = Keys.hmacShaKeyFor(secret.toByteArray())
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
        val accessTokenExpirationTimeLong = accessTokenExpirationTime.toLong()
        val createdDate = Date()
        val accessTokenExpirationDate =
            Date(createdDate.time + accessTokenExpirationTimeLong * 1000)

        val accessToken = Jwts.builder()
            .setClaims(claims)
            .setSubject(email)
            .setIssuedAt(createdDate)
            .setExpiration(accessTokenExpirationDate)
            .signWith(key)
            .compact()

        return Token(
            accessToken,
            "",
            "Bearer",
            accessTokenExpirationTimeLong,
            createdDate
        )
    }
}