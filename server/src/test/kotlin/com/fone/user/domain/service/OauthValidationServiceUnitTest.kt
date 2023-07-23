package com.fone.user.domain.service

import com.fone.common.exception.NotFoundUserException
import com.fone.user.domain.entity.OauthPrincipal
import com.fone.user.domain.entity.User
import com.fone.user.domain.enum.LoginType
import com.fone.user.domain.repository.UserRepository
import com.fone.user.infrastructure.AppleOauthRepository
import com.fone.user.infrastructure.GoogleOauthRepository
import com.fone.user.infrastructure.KakaoOauthRepository
import com.fone.user.infrastructure.NaverOauthRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot

class OauthValidationServiceUnitTest : ShouldSpec({
    val appleRepo: AppleOauthRepository = mockk {
        every { type } returns LoginType.APPLE
        coEvery { fetchPrincipal(any()) } returns OauthPrincipal(LoginType.APPLE, "user@apple.com", "apple1234")
    }
    val googleRepo: GoogleOauthRepository = mockk {
        every { type } returns LoginType.GOOGLE
        coEvery { fetchPrincipal(any()) } returns OauthPrincipal(LoginType.GOOGLE, "user@google.com")
    }
    val naverRepo: NaverOauthRepository = mockk {
        every { type } returns LoginType.NAVER
        coEvery { fetchPrincipal(any()) } returns OauthPrincipal(LoginType.NAVER, "user@naver.com")
    }
    val kakaoRepo: KakaoOauthRepository = mockk {
        every { type } returns LoginType.KAKAO
        coEvery { fetchPrincipal(any()) } returns OauthPrincipal(LoginType.KAKAO, "user@kakao.com")
    }
    val emailCapture = slot<String>()
    val typeCapture = slot<LoginType>()
    val identifierCapture = slot<String>()
    val mockFindUserRepo: UserRepository = mockk {
        coEvery { findByEmailAndLoginType(capture(emailCapture), capture(typeCapture)) } answers {
            val email = emailCapture.captured
            val type = typeCapture.captured
            val identifier = if (type == LoginType.APPLE) "apple1234" else null
            User(email = email, loginType = type, identifier = identifier)
        }
        coEvery { findByIdentifier(capture(identifierCapture)) } answers {
            val identifier = identifierCapture.captured
            User(email = "user@apple.com", loginType = LoginType.APPLE, identifier = identifier)
        }
    }
    val mockNoUserRepo: UserRepository = mockk {
        coEvery { findByEmailAndLoginType(capture(emailCapture), capture(typeCapture)) } returns null
        coEvery { findByIdentifier(capture(emailCapture)) } returns null
    }
    val oauthValidationServiceWithoutUser = OauthValidationService(
        listOf(appleRepo, googleRepo, naverRepo, kakaoRepo),
        mockNoUserRepo
    )
    val oauthValidationService = OauthValidationService(
        listOf(appleRepo, googleRepo, naverRepo, kakaoRepo),
        mockFindUserRepo
    )

    should("if isValidTokenSignIn pass it should return true") {
        oauthValidationService.isValidTokenSignIn(
            LoginType.APPLE,
            "apple token",
            "user@apple.com"
        ) shouldBe true
    }
    should("if no User it should throw") {
        shouldThrow<NotFoundUserException> {
            oauthValidationServiceWithoutUser.isValidTokenSignIn(
                LoginType.APPLE,
                "apple token",
                "user@apple.com"
            )
        }
    }

    should("isValidTokenSignUp should return true if identifier/email is equal") {
        oauthValidationService.isValidTokenSignUp(
            LoginType.GOOGLE,
            "google token",
            "user@google.com",
            null
        ) shouldBe true
        oauthValidationService.isValidTokenSignUp(
            LoginType.APPLE,
            "apple token",
            "user@apple.com",
            "apple1234"
        ) shouldBe true
    }

    should("isValidTokenSignUp should return false if identifier/email is wrong") {
        oauthValidationService.isValidTokenSignUp(
            LoginType.GOOGLE,
            "google token",
            "incorrectUser@google.com",
            null
        ) shouldBe false
        oauthValidationService.isValidTokenSignUp(
            LoginType.APPLE,
            "apple token",
            "user@apple.com",
            "orange1234"
        ) shouldBe false
    }
})
