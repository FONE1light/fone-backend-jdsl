package com.fone.user.infrastructure

import com.fone.common.IntegrationTest
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

@IntegrationTest
class NaverOauthRepositoryTest(
    private val naverOauthRepository: NaverOauthRepository,
) : ShouldSpec({
    val token = ""
    xshould("access token 활용하여 email 조회 성공") {
        val email = naverOauthRepository.fetchPrincipal(token)
        email shouldBe "imbtmn@naver.com"
    }
})
