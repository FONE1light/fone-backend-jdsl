package com.fone.filmone.common.jwt

import com.fone.filmone.common.exception.UnauthorizedException
import org.springframework.context.annotation.Bean
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.config.annotation.method.configuration.EnableReactiveMethodSecurity
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono

@EnableWebFluxSecurity
@EnableReactiveMethodSecurity
class WebSecurityConfig(
    val authenticationManager: AuthenticationManager,
    val securityContextRepository: SecurityContextRepository
) {
    @Bean
    fun securityWebFilterChain(http: ServerHttpSecurity): SecurityWebFilterChain {
        return http
            .exceptionHandling()
            .authenticationEntryPoint { swe: ServerWebExchange, _: AuthenticationException? ->
                Mono.fromRunnable {
                    throw UnauthorizedException(HttpStatus.UNAUTHORIZED, "유효하지 않은 토큰입니다.")
                }
            }.accessDeniedHandler { swe: ServerWebExchange, _: AccessDeniedException? ->
                Mono.fromRunnable { swe.response.statusCode = HttpStatus.FORBIDDEN }
            }

            .and()

            .csrf().disable()
            .formLogin().disable()
            .httpBasic().disable()

            .authenticationManager(authenticationManager)
            .securityContextRepository(securityContextRepository)

            .authorizeExchange()
            .pathMatchers(HttpMethod.OPTIONS).permitAll()
            .pathMatchers(
                "/swagger-ui/**",
                "/swagger-resources/**",
                "/v2/api-docs",
                "/webjars/**",

                "/user/v1/auth/sign-in",
                "/user/v1/auth/sign-up",
                "/user/v1/auth/check-nickname-duplication",
                "/user/v1/question",
            ).permitAll()
            .anyExchange().authenticated()

            .and()

            .build()
    }
}