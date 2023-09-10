package com.fone.common

import club.minnced.discord.webhook.WebhookClient
import club.minnced.discord.webhook.receive.ReadonlyMessage
import club.minnced.discord.webhook.send.WebhookMessage
import com.fone.sms.domain.service.AligoService
import com.fone.sms.presentation.dto.SMSSendDto.SMSSendRequest
import com.fone.sms.presentation.dto.SMSSendDto.SMSSendResponse
import com.fone.user.domain.service.OauthValidationService
import com.fone.user.presentation.dto.SignInUserDto
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import java.util.concurrent.CompletableFuture

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

    // Discord Webhook 테스트 Bean
    // 트랜젝션 중에 호출되기 때문에 테스트중에 쓰레드를 변경함
    // 제대로 작성했다면 트랜잭션 coroutine 체인을 방해해서는 안됨
    @Bean
    @Primary
    fun mockDiscordWebhookClient() = mockk<WebhookClient> {
        every { send(any<WebhookMessage>()) } answers {
            val thread = Thread {
                Thread.sleep(1000)
            }
            thread.start()
            thread.join()
            val mockMessage = mockk<ReadonlyMessage>()
            every { mockMessage.toString() } returns "Mock Message"
            CompletableFuture.completedFuture(mockMessage)
        }
    }
}
