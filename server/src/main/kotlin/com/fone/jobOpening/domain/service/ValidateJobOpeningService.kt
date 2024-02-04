package com.fone.jobOpening.domain.service

import com.fone.common.entity.ContactMethod
import com.fone.common.exception.RequestValidationException
import com.fone.jobOpening.domain.repository.LocationRepository
import com.fone.jobOpening.presentation.dto.ValidateJobOpeningDto
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ValidateJobOpeningService(
    private val locationRepository: LocationRepository,
) {
    private val emailRegex = Regex("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")

    suspend fun validateContactPage(request: ValidateJobOpeningDto.ContactPageValidation) {
        if (request.contactMethod == ContactMethod.EMAIL) {
            if (!emailRegex.matches(request.contact)) {
                throw RequestValidationException("올바른 이메일 주소를 입력해 주세요.")
            }
        } else if (request.contactMethod == ContactMethod.KAKAO) {
            if (!request.contact.startsWith("https://open.kakao.com")) {
                throw RequestValidationException("올바른 링크 주소를 입력해 주세요.")
            }
        } else {
            if (!request.contact.startsWith("https://docs.google.com/forms")) {
                throw RequestValidationException("올바른 링크 주소를 입력해 주세요.")
            }
        }
    }

    suspend fun validateTitlePage(request: ValidateJobOpeningDto.TitlePageValidation) {
        if (request.title.length < 2) {
            throw RequestValidationException("최소 2자 이상의 모집 제목을 입력해주세요.")
        }

        if (request.categories.isEmpty()) {
            throw RequestValidationException("최소 1개 이상 작품의 성격을 선택해주세요.")
        }

        validateDate(request.recruitmentStartDate, request.recruitmentEndDate)
    }

    suspend fun validateRolePage(request: ValidateJobOpeningDto.RolePageValidation) {
        if (request.casting != null) {
            if (request.casting.isBlank()) {
                throw RequestValidationException("모집 배역을 입력해 주세요.")
            }
        }

        if (request.domains != null) {
            if (request.domains.isEmpty()) {
                throw RequestValidationException("모집 분야를 선택해주세요.")
            }
        }

        if (request.numberOfRecruits == null) {
            throw RequestValidationException("모집 인원을 입력해 주세요.")
        }

        if (request.careers.isEmpty()) {
            throw RequestValidationException("경력을 1개 이상 선택해 주세요.")
        }

        if (request.ageMin == null && request.ageMax == null) {
            // 연령무관이여서 아래 검증 로직 필요 없음
            return
        }

        if (request.ageMin == null) {
            throw RequestValidationException("최소 나이를 입력해 주세요.")
        }

        if (request.ageMax == null) {
            throw RequestValidationException("최대 나이를 입력해 주세요.")
        }

        if (request.ageMin > request.ageMax) {
            throw RequestValidationException("최소 나이가 최대 나이보다 큽니다.")
        }

        if (request.ageMin < 0 || request.ageMax < 0) {
            throw RequestValidationException("나이는 0세 이상으로 입력해 주세요.")
        }

        if (request.ageMin > 200 || request.ageMax > 200) {
            throw RequestValidationException("나이는 200세 이하로 입력해 주세요.")
        }
    }

    suspend fun validateProjectPage(request: ValidateJobOpeningDto.ProjectPageValidation) {
        if (request.produce.isBlank()) {
            throw RequestValidationException("제작 주체를 입력해 주세요.")
        }
        if (request.workTitle.isBlank()) {
            throw RequestValidationException("작품 제목을 입력해 주세요.")
        }
        if (request.director.isBlank()) {
            throw RequestValidationException("연출자의 이름을 입력해 주세요.")
        }
        if (request.genres.isEmpty()) {
            throw RequestValidationException("최소 1개 이상의 장르를 선택해주세요.")
        }
    }

    suspend fun validateProjectDetailsPage(request: ValidateJobOpeningDto.ProjectDetailsPageValidation) {
        locationRepository.findLocation(request.workingCity, request.workingDistrict)
            ?: throw RequestValidationException("'시', '구'가 유효하지 않습니다.")

        validateDate(request.workingStartDate, request.workingEndDate)
    }

    suspend fun validateSummaryPage(request: ValidateJobOpeningDto.SummaryPageValidation) {
        if (request.details.length < 8) {
            throw RequestValidationException("최소 8자 이상의 상세 요강을 입력해주세요.")
        }
    }

    suspend fun validateManagerPage(request: ValidateJobOpeningDto.ManagerInfoValidation) {
        if (request.manager.isBlank()) {
            throw RequestValidationException("담당자의 이름을 입력해 주세요.")
        }
        if (!emailRegex.matches(request.email)) {
            throw RequestValidationException("올바른 이메일 주소를 입력해 주세요.")
        }
    }

    private fun validateDate(
        recruitmentStartDate: LocalDate?,
        recruitmentEndDate: LocalDate?,
    ) {
        if (recruitmentStartDate == null && recruitmentEndDate == null) {
            // 상시모집이여서 아래 검증 로직 필요 없음
            return
        }

        if (recruitmentStartDate == null) {
            throw RequestValidationException("모집기간의 시작과 끝 값을 모두 입력해 주세요.")
        }

        if (recruitmentEndDate == null) {
            throw RequestValidationException("모집기간의 시작과 끝 값을 모두 입력해 주세요.")
        }

        if (recruitmentStartDate.isAfter(recruitmentEndDate)) {
            throw RequestValidationException("마감일을 시작일보다 앞날로 입력해 주세요.")
        }

        if (!recruitmentStartDate.isAfter(LocalDate.now())) {
            throw RequestValidationException("시작일은 과거의 날짜로 설정될 수 없어요.")
        }
    }
}
