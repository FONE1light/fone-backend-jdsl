package com.fone.common

import com.fone.user.domain.service.OauthValidationService
import io.mockk.coEvery
import io.mockk.mockk
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary

@Configuration
class ConfigTestBean {
    // 테스트용 Bean, 토큰이 정상적으로 통과 시킴
    @Bean
    @Primary
    fun mockOauthValidationService() = mockk<OauthValidationService> {
        coEvery { isValidTokenSignIn(any(), any(), any()) } returns true
        coEvery { isValidTokenSignUp(any(), any(), any(), any()) } returns true
    }
}
