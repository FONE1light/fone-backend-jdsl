package com.fone.jobOpening.presentation.dto

import com.fone.common.entity.Career
import com.fone.common.entity.CategoryType
import com.fone.common.entity.Gender
import com.fone.common.entity.Genre
import com.fone.common.entity.Salary
import com.fone.common.entity.Weekday
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

class ValidateJobOpeningDto {
    data class TitlePageValidation(
        @Schema(description = "모집제목", example = "많은 이들의 시선보다 ..") val title: String?,
        @Schema(description = "작품의 성격", example = "[\"ACTOR\",\"ACTRESS\"]") val categories: List<CategoryType>?,
        @DateTimeFormat(pattern = "yyyy.MM.dd")
        val recruitmentStartDate: LocalDate?,
        @Schema(
            description = "모집 기간 종료일(null이면 상시모집)",
            example = "2021.10.11"
        )
        @DateTimeFormat(pattern = "yyyy.MM.dd")
        val recruitmentEndDate: LocalDate?,
        @Schema(
            description = "이미지",
            example = "[\"https://www.naver.com\",\"https://www.naver.com\"]"
        )
        val imageUrls: List<String>?,
    )

    data class RolePageValidation(
        @Schema(description = "모집배역", example = "30대 중반 경찰")
        val casting: String?,
        @Schema(description = "모집인원", example = "1")
        val numberOfRecruits: Int?,
        @Schema(description = "성별", example = "MAN")
        val gender: Gender?,
        @Schema(description = "최대 나이", example = "40")
        val ageMax: Int?,
        @Schema(description = "최소 나이", example = "20")
        val ageMin: Int?,
        @Schema(description = "경력", example = "NEWCOMER")
        val career: Career?,
    )

    data class InfoPageValidation(
        @Schema(description = "모집배역", example = "30대 중반 경찰")
        val casting: String?,
        @Schema(description = "모집인원", example = "1")
        val numberOfRecruits: Int?,
        @Schema(description = "성별", example = "MAN")
        val gender: Gender?,
        @Schema(description = "최대 나이", example = "40")
        val ageMax: Int?,
        @Schema(description = "최소 나이", example = "20")
        val ageMin: Int?,
        @Schema(description = "경력", example = "NEWCOMER")
        val career: Career?,
    )

    data class ProjectPageValidation(
        @Schema(description = "제작", example = "FONE") val produce: String?,
        @Schema(description = "제목", example = "작품 제목") val workTitle: String?,
        @Schema(description = "연출", example = "이하은") val director: String?,
        @Schema(description = "장르", example = "[\"스릴러\",\"드라마\"]") val genres: Set<Genre>?,
        @Schema(description = "로그라인", example = "자폐 스펙트럼 장애를 가진 변호사의 이야기") val logline: String?,
    )

    data class ProjectDetailsPageValidation(
        @Schema(description = "촬영위치(지역)", example = "서울특별시")
        val workingCity: String?,
        @Schema(description = "촬영위치(시군구)", example = "강남구")
        val workingDistrict: String?,
        @Schema(
            description = "근무기간(시작일)",
            example = "2021-10-10"
        )
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        val workingStartDate: LocalDate?,
        @Schema(
            description = "근무기간(종료일)",
            example = "2021-10-11"
        )
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        val workingEndDate: LocalDate? = null,
        @Schema(description = "근무요일", example = "[\"MON\",\"TUE\"]") val selectedDays: Set<Weekday>?,
        @Schema(
            description = "근무시간(시작시간)",
            example = "09:00"
        ) val workingStartTime: String?,
        @Schema(
            description = "근무시간(종료시간)",
            example = "18:00"
        ) val workingEndTime: String?,
        @Schema(
            description = "급여유형",
            example = "HOURLY"
        ) val salaryType: Salary?,
        @Schema(description = "급여", example = "100000") val salary: Int?,
    )

    data class SummaryPageValidation(
        @Schema(description = "상세요강", example = "상세내용") val details: String? = "",
    )

    data class ManagerInfoValidation(
        @Schema(description = "담당자", example = "김매니저") val manager: String?,
        @Schema(description = "이메일", example = "test@test.com") val email: String?,
    )
}