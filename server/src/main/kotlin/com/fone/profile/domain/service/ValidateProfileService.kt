package com.fone.profile.domain.service

import com.fone.common.exception.RequestValidationException
import com.fone.profile.presentation.dto.FifthPage
import com.fone.profile.presentation.dto.FourthPage
import com.fone.profile.presentation.dto.SecondPage
import com.fone.profile.presentation.dto.SixthPage
import com.fone.profile.presentation.dto.ThirdPage
import com.fone.user.domain.repository.UserRepository
import org.hibernate.validator.internal.constraintvalidators.hv.URLValidator
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ValidateProfileService(
    private val userRepository: UserRepository,
) {
    private val urlValidator = URLValidator()
    private val emailRegex = Regex("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")

    fun validateBasicPage(request: SecondPage) {
        if (request.name.isBlank()) {
            throw RequestValidationException("본인의 이름을 입력해 주세요.")
        }
        if (request.hookingComment.length < 2) {
            throw RequestValidationException("최소 2자 이상의 후킹 멘트를 입력해주세요.")
        }
        if (request.profileImages.isEmpty()) {
            throw RequestValidationException("프로필 이미지를 첨부해 주세요.")
        }
        if (request.profileImages.any { urlValidator.isValid(it, null) }) {
            throw RequestValidationException("프로필 이미지에 유효하지 않은 값이 있습니다.")
        }
    }

    suspend fun validateDetailPage(
        email: String,
        request: ThirdPage,
    ) {
        if (LocalDate.now().year - request.birthday.year > 100) {
            throw RequestValidationException("100세 이상의 나이는 입력할 수 없습니다.")
        }

        if (request.birthday.isAfter(LocalDate.now())) {
            throw RequestValidationException("미래의 날짜를 입력할 수 없습니다.")
        }

        if (request.gender == null) {
            throw RequestValidationException("성별을 선택해 주세요.")
        }

        if (request.height != null) {
            if (request.height < 0) {
                throw RequestValidationException("신장을 입력해 주세요.")
            }
        }

        if (request.weight != null) {
            if (request.weight < 0) {
                throw RequestValidationException("체중을 입력해 주세요.")
            }
        }

        if (!emailRegex.matches(request.email)) {
            throw RequestValidationException("올바른 이메일 주소를 입력해 주세요.")
        }

        if (request.domains != null) {
            if (request.domains.isEmpty()) {
                throw RequestValidationException("등록 분야를 선택해주세요.")
            }
        }
    }

    fun validateDescriptionPage(fourthPage: FourthPage) {
        if (fourthPage.details.length < 8) {
            throw RequestValidationException("최소 8자 이상의 상세 요강을 입력해주세요.")
        }
    }

    fun validateCareerPage(fifthPage: FifthPage) {
        // 검증 할 것 없음
        return
    }

    fun validateInterestPage(sixthPage: SixthPage) {
        // 검증 할 것 없음
        return
    }
}
