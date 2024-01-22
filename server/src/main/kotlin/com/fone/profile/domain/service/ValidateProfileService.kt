package com.fone.profile.domain.service

import com.fone.common.exception.NotFoundUserException
import com.fone.common.exception.RequestValidationException
import com.fone.profile.presentation.dto.ValidateProfileDto
import com.fone.user.domain.enum.Job
import com.fone.user.domain.repository.UserRepository
import org.hibernate.validator.internal.constraintvalidators.hv.URLValidator
import org.springframework.stereotype.Service

@Service
class ValidateProfileService(
    private val userRepository: UserRepository,
) {
    private val urlValidator = URLValidator()

    fun validateBasicPage(basicPageValidation: ValidateProfileDto.BasicPageValidation) {
        if (basicPageValidation.name.isNullOrBlank()) {
            throw RequestValidationException("항목 '이름'이 비었습니다.")
        }
        if (basicPageValidation.hookingComment.isNullOrEmpty()) {
            throw RequestValidationException("항목 '후킹멘트'가 비었습니다.")
        }
        if (basicPageValidation.profileImages.isNullOrEmpty()) {
            throw RequestValidationException("항목 '이미지 첨부'가 비었습니다.")
        }
        if (basicPageValidation.profileImages.any { urlValidator.isValid(it, null) }) {
            throw RequestValidationException("항목 '이미지 첨부'에 유효하지 않은 값이 있습니다.")
        }
    }

    suspend fun validateDetailPage(
        email: String,
        detailPageValidation: ValidateProfileDto.DetailPageValidation,
    ) {
        val user = userRepository.findByEmail(email) ?: throw NotFoundUserException()
        if (detailPageValidation.birthday == null) {
            throw RequestValidationException("항목 '출생연도'가 비었습니다.")
        }
        if (detailPageValidation.gender == null) {
            throw RequestValidationException("항목 '성별'이 비었습니다.")
        }
        if (user.job == Job.STAFF) {
            if (detailPageValidation.domains.isNullOrEmpty()) {
                throw RequestValidationException("항목 '도메인'이 비었습니다.")
            }
        } else {
            if (detailPageValidation.height == null) {
                throw RequestValidationException("항목 '신장'이 비었습니다.")
            }
            if (detailPageValidation.weight == null) {
                throw RequestValidationException("항목 '체중'이 비었습니다.")
            }
        }
        if (detailPageValidation.email == null) {
            throw RequestValidationException("항목 '성별'이 비었습니다.")
        }
    }

    fun validateDescriptionPage(descriptionPageValidation: ValidateProfileDto.DescriptionPageValidation) {
        if (descriptionPageValidation.details.isNullOrBlank()) {
            throw RequestValidationException("항목 '상세요강'이 비었습니다.")
        }
        if (descriptionPageValidation.details.length > 200) {
            throw RequestValidationException("항목 '상세요강'이 200자 넘습니다.")
        }
    }

    fun validateCareerPage(careerPageValidation: ValidateProfileDto.CareerPageValidation) {
        if (careerPageValidation.career == null) {
            throw RequestValidationException("항목 '경력'이 비었습니다.")
        }
        if (careerPageValidation.careerDetail.isNullOrBlank()) {
            throw RequestValidationException("항목 '경력 상세사항'이 비었습니다.")
        }
        if (careerPageValidation.careerDetail.length > 500) {
            throw RequestValidationException("항목 '경력 상세사항'이 500자 넘습니다.")
        }
    }

    fun validateInterestPage(interestPageValidation: ValidateProfileDto.InterestPageValidation) {
        if (interestPageValidation.categories.isNullOrEmpty()) {
            throw RequestValidationException("항목 '관심사'가 비었습니다.")
        }
    }
}
