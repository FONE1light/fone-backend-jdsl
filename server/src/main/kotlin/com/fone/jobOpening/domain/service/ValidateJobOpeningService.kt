package com.fone.jobOpening.domain.service

import com.fone.common.exception.RequestValidationException
import com.fone.jobOpening.domain.repository.LocationRepository
import com.fone.jobOpening.presentation.dto.ValidateJobOpeningDto
import org.springframework.stereotype.Service

@Service
class ValidateJobOpeningService(
    private val locationRepository: LocationRepository,
) {
    private val emailRegex = Regex("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$")

    suspend fun validateTitlePage(request: ValidateJobOpeningDto.TitlePageValidation) {
        if (request.title.isNullOrBlank()) {
            throw RequestValidationException("'모집제목' 항목이 비어있습니다.")
        }
        if (request.categories.isNullOrEmpty()) {
            throw RequestValidationException("'작품의 성격' 항목이 비어있습니다.")
        }
    }

    suspend fun validateRolePage(request: ValidateJobOpeningDto.RolePageValidation) {
        if (request.casting.isNullOrBlank()) {
            throw RequestValidationException("'모집배역' 항목이 비어있습니다.")
        }
        if (request.numberOfRecruits == null) {
            throw RequestValidationException("'모집인원' 항목이 비어있습니다.")
        }
    }

    suspend fun validateProjectPage(request: ValidateJobOpeningDto.ProjectPageValidation) {
        if (request.produce.isNullOrBlank()) {
            throw RequestValidationException("'제작' 항목이 비어있습니다.")
        }
        if (request.workTitle.isNullOrBlank()) {
            throw RequestValidationException("'제목' 항목이 비어있습니다.")
        }
        if (request.director.isNullOrBlank()) {
            throw RequestValidationException("'연출' 항목이 비어있습니다.")
        }
        if (request.genres.isNullOrEmpty()) {
            throw RequestValidationException("'장르' 항목이 비어있습니다.")
        }
    }

    suspend fun validateProjectDetailsPage(request: ValidateJobOpeningDto.ProjectDetailsPageValidation) {
        if (!request.workingCity.isNullOrBlank() || !request.workingDistrict.isNullOrBlank()) {
            if (request.workingCity.isNullOrBlank() || request.workingDistrict.isNullOrBlank()) {
                throw RequestValidationException("'시', '구' 모두 채워져야합니다.")
            }
            locationRepository.findLocation(request.workingCity, request.workingDistrict)
                ?: throw RequestValidationException("'시', '구'가 유효하지 않습니다.")
        }
    }

    suspend fun validateSummaryPage(request: ValidateJobOpeningDto.SummaryPageValidation) {
        if (request.details.isNullOrBlank()) {
            throw RequestValidationException("'상세요강' 항목이 비어있습니다.")
        }
        if (request.details.length > 500) {
            throw RequestValidationException("'상세요강' 500자 넘습니다.")
        }
    }

    suspend fun validateManagerPage(request: ValidateJobOpeningDto.ManagerInfoValidation) {
        if (request.manager.isNullOrBlank()) {
            throw RequestValidationException("'담당자' 항목이 비어있습니다.")
        }
        if (request.email.isNullOrBlank()) {
            throw RequestValidationException("'이메일' 항목이 비어있습니다.")
        }
        if (emailRegex.matches(request.email)) {
            throw RequestValidationException("'이메일' 형식이 맞지 않습니다.")
        }
    }
}
