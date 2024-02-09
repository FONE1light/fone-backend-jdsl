package com.fone.jobOpening.presentation.dto

import com.fone.common.entity.Career
import com.fone.common.entity.CategoryType
import com.fone.common.entity.ContactMethod
import com.fone.common.entity.DomainType
import com.fone.common.entity.Gender
import com.fone.common.entity.Genre
import com.fone.common.entity.Salary
import com.fone.common.entity.Weekday
import io.swagger.annotations.ApiModelProperty
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

class ValidateJobOpeningDto {
    data class FirstPage(
        @Schema(description = "연락방법", example = "KAKAO")
        @ApiModelProperty(value = "연락방법", example = "KAKAO")
        val contactMethod: ContactMethod,

        @Schema(description = "연락처", example = "https://docs.google.com/forms/...")
        @ApiModelProperty(value = "연락처", example = "https://docs.google.com/forms/...")
        val contact: String,
    )

    data class SecondPage(
        @Schema(description = "모집제목", example = "많은 이들의 시선보다 ..")
        @ApiModelProperty(value = "모집제목", example = "많은 이들의 시선보다 ..")
        val title: String,

        @Schema(
            description = "작품의 성격",
            example = "[\"WEB_DRAMA\",\"FEATURE_FILM\"]"
        )
        @ApiModelProperty(
            value = "작품의 성격",
            example = "[\"WEB_DRAMA\",\"FEATURE_FILM\"]"
        )
        val categories: List<CategoryType> = emptyList(),

        @Schema(
            description = "모집 기간 시작일 (시작일, 종료일 둘다 null이면 상시모집)",
            example = "2021-10-11"
        )
        @ApiModelProperty(value = "모집 기간 시작일 (시작일, 종료일 둘다 null이면 상시모집)", example = "2021-10-11")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        val recruitmentStartDate: LocalDate?,

        @Schema(
            description = "모집 기간 종료일 (시작일, 종료일 둘다 null이면 상시모집)",
            example = "2021-10-11"
        )
        @ApiModelProperty(value = "모집 기간 종료일 (시작일, 종료일 둘다 null이면 상시모집)", example = "2021-10-11")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        val recruitmentEndDate: LocalDate?,

        @Schema(
            description = "이미지",
            example = "[\"https://www.naver.com\",\"https://www.naver.com\"]"
        )
        @ApiModelProperty(value = "이미지", example = "[\"https://www.naver.com\",\"https://www.naver.com\"]")
        val imageUrls: List<String> = emptyList(),

        @Schema(
            description = "대표이미지",
            example = "https://www.naver.com"
        )
        @ApiModelProperty(value = "대표이미지 (이미지 첨부 안할시 null로 전달)", example = "https://www.naver.com")
        val representativeImageUrl: String? = null,
    )

    data class ThirdPage(
        @Schema(description = "모집배역 (스태프에서는 null)", example = "30대 중반 경찰")
        @ApiModelProperty(value = "모집배역 (스태프에서는 null)", example = "30대 중반 경찰")
        val casting: String?,

        @Schema(description = "분야 (배우에서는 null)", example = "[\"SCENARIO\",\"DIRECTOR\"]")
        @ApiModelProperty(value = "분야 (배우에서는 null)", example = "[\"SCENARIO\",\"DIRECTOR\"]")
        val domains: List<DomainType>?,

        @Schema(description = "모집인원 (입력안할 시 -값으로 설정)", example = "1")
        @ApiModelProperty(value = "모집인원 (입력안할 시 -값으로 설정)", example = "1")
        val numberOfRecruits: Int = -1,

        @Schema(description = "성별 (enum값에 성별무관 있음)", example = "MAN")
        @ApiModelProperty(value = "성별 (enum값에 성별무관 있음)", example = "MAN")
        val gender: Gender,

        @Schema(description = "최대 나이 (최대,최소 둘다 null이면 연령무관, '시작'이면 1값 입력)", example = "40")
        @ApiModelProperty(value = "최대 나이 (최대,최소 둘다 null이면 연령무관, '시작'이면 1값 입력)", example = "40")
        val ageMax: Int?,

        @Schema(description = "최소 나이 (최대,최소 둘다 null이면 연령무관, '끝'이면 200값 입력)", example = "20")
        @ApiModelProperty(value = "최소 나이 (최대,최소 둘다 null이면 연령무관, '끝'이면 200값 입력)", example = "20")
        val ageMin: Int?,

        @Schema(description = "경력", example = "[\"NEWCOMER\"]")
        @ApiModelProperty(value = "경력", example = "[\"NEWCOMER\"]")
        val careers: List<Career> = emptyList(),
    )

    data class FourthPage(
        @Schema(description = "제작", example = "FONE")
        @ApiModelProperty(value = "제작", example = "FONE")
        val produce: String,

        @Schema(description = "제목", example = "작품 제목")
        @ApiModelProperty(value = "제목", example = "작품 제목")
        val workTitle: String,

        @Schema(description = "연출", example = "이하은")
        @ApiModelProperty(value = "연출", example = "이하은")
        val director: String,

        @Schema(description = "장르", example = "[\"ACTION\",\"DRAMA\"]")
        @ApiModelProperty(value = "장르", example = "[\"ACTION\",\"DRAMA\"]")
        val genres: Set<Genre> = emptySet(),

        @Schema(description = "로그라인 (null 이면 비공개)", example = "자폐 스펙트럼 장애를 가진 변호사의 이야기")
        @ApiModelProperty(value = "로그라인 (null 이면 비공개)", example = "자폐 스펙트럼 장애를 가진 변호사의 이야기")
        val logline: String?,
    )

    data class FifthPage(
        @Schema(description = "촬영위치(지역) (workingCity, workingDistrict '전체'로 요청하면 미정)", example = "서울특별시")
        @ApiModelProperty(
            value = "촬영위치(지역) (workingCity, workingDistrict '전체'로 요청하면 미정)",
            example = "서울특별시"
        )
        val workingCity: String,

        @Schema(
            description = "촬영위치(시군구) (workingDistrict를 특정시에 대해서 '전체'로 요청할 수 있음)",
            example = "도봉구"
        )
        @ApiModelProperty(
            value = "촬영위치(시군구) (workingDistrict를 특정시에 대해서 '전체'로 요청할 수 있음)",
            example = "도봉구"
        )
        val workingDistrict: String,

        @Schema(
            description = "근무기간 시작일 (시작일, 종료일 둘다 null이면 미정)",
            example = "2021-10-10"
        )
        @ApiModelProperty(value = "근무기간 시작일 (시작일, 종료일 둘다 null이면 미정)", example = "2021-10-10")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        val workingStartDate: LocalDate? = null,

        @Schema(
            description = "근무기간 종료일 (시작일, 종료일 둘다 null이면 미정)",
            example = "2021-10-11"
        )
        @ApiModelProperty(value = "근무기간 종료일 (시작일, 종료일 둘다 null이면 미정)", example = "2021-10-11")
        @DateTimeFormat(pattern = "yyyy-MM-dd")
        val workingEndDate: LocalDate? = null,

        @Schema(description = "근무요일 (추후협의 값 enum에 있음)", example = "[\"MON\",\"TUE\"]")
        @ApiModelProperty(value = "근무요일 (추후협의 값 enum에 있음)", example = "[\"MON\",\"TUE\"]")
        val selectedDays: Set<Weekday> = emptySet(),

        @Schema(
            description = "근무시간 시작 (시작, 종료 둘다 null이면 미정)",
            example = "09:00"
        )
        @ApiModelProperty(value = "근무시간 시작 (시작, 종료 둘다 null이면 미정)", example = "09:00")
        val workingStartTime: String?,

        @Schema(
            description = "근무시간 종료 (시작, 종료 둘다 null이면 미정)",
            example = "18:00"
        )
        @ApiModelProperty(value = "근무시간 종료 (시작, 종료 둘다 null이면 미정)", example = "18:00")
        val workingEndTime: String?,

        @Schema(
            description = "급여유형 (추후협의 값 enum에 있음)",
            example = "HOURLY"
        )
        @ApiModelProperty(value = "급여유형 (추후협의 값 enum에 있음)", example = "HOURLY")
        val salaryType: Salary,

        @Schema(description = "급여 (입력 안할시 -값으로 전달)", example = "100000")
        @ApiModelProperty(value = "급여 (입력 안할시 -값으로 전달)", example = "100000")
        val salary: Int,
    )

    data class SixthPage(
        @Schema(description = "상세요강", example = "상세내용")
        @ApiModelProperty(value = "상세요강", example = "상세내용")
        val details: String,
    )

    data class SeventhPage(
        @Schema(description = "담당자", example = "김매니저")
        @ApiModelProperty(value = "담당자", example = "김매니저")
        val manager: String,

        @Schema(description = "이메일", example = "test@test.com")
        @ApiModelProperty(value = "이메일", example = "test@test.com")
        val email: String,
    )
}
