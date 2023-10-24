package com.fone.user.infrastructure

import com.fone.common.IntegrationTest
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

@IntegrationTest
class AppleOauthRepositoryTest(
    private val appleOauthRepository: AppleOauthRepository,
) : ShouldSpec({
    val token = ""
    xshould("access token 활용하여 email 조회 성공") {
        val email = appleOauthRepository.fetchPrincipal(token)
        email shouldBe "imbtmn@naver.com"
    }
})
