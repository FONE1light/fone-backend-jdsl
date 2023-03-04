package com.fone.common.jwt

import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextImpl
import org.springframework.security.web.server.context.ServerSecurityContextRepository
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@Component
class SecurityContextRepository(val authenticationManager: AuthenticationManager) :
    ServerSecurityContextRepository {
    override fun save(swe: ServerWebExchange, sc: SecurityContext): Mono<Void> {
        throw UnsupportedOperationException("Not supported yet.")
    }

    override fun load(swe: ServerWebExchange): Mono<SecurityContext> {
        val request = swe.request
        val authHeader = request.headers.getFirst(HttpHeaders.AUTHORIZATION)
        var authToken: String? = null

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            authToken = authHeader.substring(7)
        }

        if (authToken == null) {
            return Mono.empty()
        }

        val auth: Authentication = UsernamePasswordAuthenticationToken(authToken, authToken)
        val authentication = authenticationManager.authenticate(auth)
        return authentication.map { SecurityContextImpl(it) }
    }
}
