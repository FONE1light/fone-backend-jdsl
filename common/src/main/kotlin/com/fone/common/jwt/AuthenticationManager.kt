package com.fone.common.jwt

import java.util.stream.Collectors
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono

@Component
class AuthenticationManager(
    val jwtUtils: JWTUtils,
    val userRepository: JwtUserRepository,
) : ReactiveAuthenticationManager {

    override fun authenticate(authentication: Authentication): Mono<Authentication> {
        val authToken = authentication.credentials.toString()
        var email: String?
        try {
            email = jwtUtils.getEmailFromToken(authToken)
        } catch (e: Exception) {
            return Mono.empty()
        }

        runBlocking {
            return@runBlocking async { userRepository.validTokenByEmail(email) }.await()
        }
            ?: return Mono.empty()

        return Mono.just(jwtUtils.validateToken(authToken))
            .filter { valid -> valid }
            .switchIfEmpty(Mono.empty())
            .map {
                val claims = jwtUtils.getAllClaimsFromToken(authToken)
                val rolesMap = claims.get("roles", java.util.List::class.java)
                UsernamePasswordAuthenticationToken(
                    email,
                    null,
                    rolesMap
                        .stream()
                        .map { role -> SimpleGrantedAuthority(role as String) }
                        .collect(Collectors.toList())
                )
            }
    }
}
