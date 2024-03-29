package com.fone.user.infrastructure

import com.fone.common.IntegrationTest
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

@IntegrationTest
class GoogleOauthRepositoryTest(
    private val googleOauthRepository: GoogleOauthRepository,
) : ShouldSpec({
    val token = ""
    xshould("id token 활용하여 email 조회 성공") {
        val principal = googleOauthRepository.fetchPrincipal(token)
        principal.email shouldBe "fyimbtmn@gmail.com"
    }
})
