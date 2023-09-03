package com.fone.common

import com.fone.sms.domain.service.AligoService
import com.fone.sms.presentation.dto.SMSSendRequest
import com.fone.sms.presentation.dto.SMSSendResponse
import com.fone.user.domain.service.OauthValidationService
import com.fone.user.presentation.dto.SignInUserDto
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
        coEvery { getEmail(any()) } answers {
            val request = it.invocation.args.first() as SignInUserDto.SocialSignInUserRequest
            request.accessToken.split(":").last()
        }
    }

    // SMS 전송 테스트 bean.
    @Bean
    @Primary
    fun mockSMSRepository() = mockk<AligoService> {
        coEvery { sendToAligo(any<SMSSendRequest>()) } returns SMSSendResponse("123")
    }
}
