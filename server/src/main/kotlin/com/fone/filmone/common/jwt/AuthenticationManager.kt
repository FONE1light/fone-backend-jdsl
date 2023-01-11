package com.fone.filmone.common.jwt

import org.springframework.security.authentication.ReactiveAuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.stream.Collectors

@Component
class AuthenticationManager(
    val jwtUtils: JWTUtils
) : ReactiveAuthenticationManager {

    override fun authenticate(authentication: Authentication): Mono<Authentication> {
        val authToken = authentication.credentials.toString()
        var email: String?
        try {
            email = jwtUtils.getEmailFromToken(authToken)
        } catch (e: Exception) {
            return Mono.empty()
        }

        return Mono.just(jwtUtils.validateToken(authToken))
            .filter { valid -> valid }
            .switchIfEmpty(Mono.empty())
            .map {
                val claims = jwtUtils.getAllClaimsFromToken(authToken)
                val rolesMap = claims.get("roles", java.util.List::class.java)
                UsernamePasswordAuthenticationToken(
                    email,
                    null,
                    rolesMap.stream()
                        .map { role ->
                            SimpleGrantedAuthority(
                                role as String
                            )
                        }
                        .collect(Collectors.toList())
                )
            }
    }
}