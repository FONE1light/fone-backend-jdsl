package com.fone.filmone.common.jwt

import com.fone.filmone.domain.user.UserInfo
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import java.security.Key
import java.util.*
import javax.annotation.PostConstruct

object JWTUtils {
    @Value("\${jwt.secret}")
    private lateinit var secret: String

    @Value("\${jwt.access-token-validity-in-seconds}")
    private lateinit var accessTokenExpirationTime: String

    private lateinit var key: Key

    @PostConstruct
    fun init() {
        key = Keys.hmacShaKeyFor(secret.toByteArray())
    }

    fun getAllClaimsFromToken(token: String?): Claims {
        return Jwts.parser().setSigningKey(key).parseClaimsJws(token).body
    }

    fun getUsernameFromToken(token: String?): String {
        return getAllClaimsFromToken(token).subject
    }

    fun getExpirationDateFromToken(token: String?): Date {
        return getAllClaimsFromToken(token).expiration
    }

    private fun isTokenExpired(token: String): Boolean {
        val expiration = getExpirationDateFromToken(token)
        return expiration.before(Date())
    }

    fun generateToken(userInfo: UserInfo.Main): UserInfo.Token {
        val claims: MutableMap<String, Any?> = HashMap()
        claims["role"] = userInfo.roles
        return doGenerateToken(claims, userInfo.loginId)
    }

    private fun doGenerateToken(claims: Map<String, Any?>, username: String): UserInfo.Token {
        val accessTokenExpirationTimeLong = accessTokenExpirationTime.toLong()
        val createdDate = Date()
        val accessTokenExpirationDate =
            Date(createdDate.time + accessTokenExpirationTimeLong * 1000)

        val accessToken = Jwts.builder()
            .setClaims(claims)
            .setSubject(username)
            .setIssuedAt(createdDate)
            .setExpiration(accessTokenExpirationDate)
            .signWith(key)
            .compact()

        return UserInfo.Token(
            accessToken,
            "Bearer",
            accessTokenExpirationTimeLong,
            createdDate
        )
    }

    fun validateToken(token: String): Boolean {
        return !isTokenExpired(token)
    }
}