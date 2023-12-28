package com.fone.jobOpening.presentation.dto.common

import com.fone.common.entity.Genre
import com.fone.common.entity.Salary
import com.fone.common.entity.Weekday
import com.fone.jobOpening.domain.entity.Location
import com.fone.jobOpening.domain.entity.Work
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

data class WorkDto(
    @Schema(description = "제작", example = "FONE") val produce: String = "",
    @Schema(description = "제목", example = "작품 제목") val workTitle: String = "",
    @Schema(description = "연출", example = "이하은") val director: String = "",
    @Schema(description = "로그라인", example = "자폐 스펙트럼 장애를 가진 변호사의 이야기") val logline: String? = null,
    @Schema(description = "상세요강", example = "상세내용") val details: String = "",
    @Schema(description = "담당자", example = "김매니저") val manager: String = "",
    @Schema(description = "이메일", example = "test@test.com") val email: String = "",
    @Schema(description = "장르", example = "[\"스릴러\",\"드라마\"]") val genres: Set<Genre> = setOf(),
    @Schema(description = "촬영위치(지역)", example = "서울특별시")
    val workingCity: String = "",
    @Schema(description = "촬영위치(시군구)", example = "강남구")
    val workingDistrict: String = "",
    @Schema(
        description = "근무기간(시작일)",
        example = "2021-10-10"
    )
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    val workingStartDate: LocalDate? = null,
    @Schema(
        description = "근무기간(종료일)",
        example = "2021-10-11"
    )
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    val workingEndDate: LocalDate? = null,
    @Schema(description = "근무요일", example = "[\"MON\",\"TUE\"]") val selectedDays: Set<Weekday> = setOf(),
    @Schema(
        description = "근무시간(시작시간)",
        example = "09:00"
    ) val workingStartTime: String? = null,
    @Schema(
        description = "근무시간(종료시간)",
        example = "18:00"
    ) val workingEndTime: String? = null,
    @Schema(
        description = "급여유형",
        example = "HOURLY"
    ) val salaryType: Salary = Salary.HOURLY,
    @Schema(description = "급여", example = "100000") val salary: Int = 0,
) {
    @get:Schema(description = "근무기간", example = "2021.10.10(금) ~ 2023.10.17(화)")
    val workingDate: String
        get() =
            if (workingStartDate == null || workingEndDate == null) {
                "미정"
            } else {
                workingStartDate.format(
                    DateTimeFormatter.ofPattern(
                        "yyyy.MM.dd(E)",
                        Locale.KOREAN
                    )
                ) + " ~ " + workingEndDate.format(DateTimeFormatter.ofPattern("yyyy.MM.dd(E)", Locale.KOREAN))
            }

    @get:Schema(description = "촬영위치", example = "서울시 강남구")
    val workingLocation: String
        get() =
            if (workingCity == "" || workingDistrict == "") {
                "미정"
            } else {
                "$workingCity $workingDistrict"
            }

    @get:Schema(description = "근무시간", example = "09:00 ~ 18:00")
    val workingTime: String
        get() =
            if (workingStartTime == null || workingEndTime == null) {
                "미정"
            } else {
                workingStartTime.format(DateTimeFormatter.ofPattern("HH:mm")) + " ~ " +
                    workingEndTime.format(
                        DateTimeFormatter.ofPattern("HH:mm")
                    )
            }

    constructor(
        work: Work,
        location: Location?,
    ) : this(
        produce = work.produce,
        workTitle = work.workTitle,
        director = work.director,
        logline = work.logline,
        details = work.details,
        manager = work.manager,
        email = work.email,
        genres = work.genres.map { Genre(it) }.toSet(),
        workingStartDate = work.workingStartDate,
        workingEndDate = work.workingEndDate,
        selectedDays = work.selectedDays.map { Weekday(it) }.toSet(),
        workingStartTime = work.workingStartTime,
        workingEndTime = work.workingEndTime,
        workingCity = location?.region ?: "",
        workingDistrict = location?.district ?: "",
        salaryType = work.salaryType,
        salary = work.salary
    )

    fun toEntity(): Work {
        return Work(
            produce = produce,
            workTitle = workTitle,
            director = director,
            logline = logline,
            details = details,
            manager = manager,
            email = email,
            genres = genres.map { it.toString() },
            workingStartDate = workingStartDate,
            workingEndDate = workingEndDate,
            selectedDays = selectedDays.map { it.toString() },
            workingStartTime = workingStartTime,
            workingEndTime = workingEndTime,
            salaryType = salaryType,
            salary = salary
        )
    }
}
