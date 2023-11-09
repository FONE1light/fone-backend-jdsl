package com.fone.jobOpening.presentation.dto.common

import com.fone.common.entity.Genre
import com.fone.common.entity.Salary
import com.fone.common.entity.Weekday
import com.fone.jobOpening.domain.entity.Work
import io.swagger.v3.oas.annotations.media.Schema
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate
import java.time.LocalTime

data class WorkDto(
    @Schema(description = "제작", example = "FONE")
    val produce: String = "",
    @Schema(description = "제목", example = "작품 제목")
    val workTitle: String = "",
    @Schema(description = "연출", example = "이하은")
    val director: String = "",
    @Schema(description = "장르", example = "스릴러")
    @Deprecated("genres 로 대체합니다.")
    val genre: String = "",
    @Schema(description = "로그라인", example = "자폐 스펙트럼 장애를 가진 변호사의 이야기")
    val logline: String? = null,
    @Schema(description = "촬영위치", example = "서울")
    @Deprecated("workingCity, workingDistrict 로 대체합니다.")
    val location: String? = null,
    @Schema(description = "촬영기간", example = "2021-10-10 ~ 2021-10-11")
    @Deprecated("workingStartDate, workingEndDate 로 대체합니다.")
    val period: String? = null,
    @Schema(description = "급여", example = "추후협의")
    @Deprecated("salaryType, salary 로 대체합니다.")
    val pay: String? = null,
    @Schema(description = "상세요강", example = "상세내용")
    val details: String = "",
    @Schema(description = "담당자", example = "김매니저")
    val manager: String = "",
    @Schema(description = "이메일", example = "test@test.com")
    val email: String = "",

    @Schema(description = "장르", example = "[\"스릴러\",\"드라마\"]")
    val genres: Set<Genre> = setOf(),
    @Schema(description = "촬영위치(시)", example = "서울")
    val workingCity: String = "",
    @Schema(description = "촬영위치(구)", example = "강남구")
    val workingDistrict: String = "",
    @Schema(description = "근무기간(시작일)", example = "2021.10.10")
    @DateTimeFormat(pattern = "yyyy.MM.dd")
    val workingStartDate: LocalDate? = null,
    @Schema(description = "근무기간(종료일)", example = "2021.10.11")
    @DateTimeFormat(pattern = "yyyy.MM.dd")
    val workingEndDate: LocalDate? = null,
    @Schema(description = "근무요일", example = "[\"MON\",\"TUE\"]")
    val selectedDays: Set<Weekday> = setOf(),
    @Schema(description = "근무시간(시작시간)", example = "09:00")
    @DateTimeFormat(pattern = "HH:mm")
    val workingStartTime: LocalTime? = null,
    @Schema(description = "근무시간(종료시간)", example = "18:00")
    @DateTimeFormat(pattern = "HH:mm")
    val workingEndTime: LocalTime? = null,
    @Schema(description = "급여유형", example = "HOUR")
    val salaryType: Salary = Salary.HOURLY,
    @Schema(description = "급여", example = "100000")
    val salary: Int = 0,
) {
    constructor(
        work: Work,
    ) : this(
        produce = work.produce,
        workTitle = work.workTitle,
        director = work.director,
        genre = work.genre,
        logline = work.logline,
        location = work.location,
        period = work.period,
        pay = work.pay,
        details = work.details,
        manager = work.manager,
        email = work.email,
        genres = work.genres,
        workingCity = work.workingCity,
        workingDistrict = work.workingDistrict,
        workingStartDate = work.workingStartDate,
        workingEndDate = work.workingEndDate,
        selectedDays = work.selectedDays,
        workingStartTime = work.workingStartTime,
        workingEndTime = work.workingEndTime,
        salaryType = work.salaryType,
        salary = work.salary
    )

    fun toEntity(): Work {
        return Work(
            produce = produce,
            workTitle = workTitle,
            director = director,
            genre = genre,
            logline = logline,
            location = location,
            period = period,
            pay = pay,
            details = details,
            manager = manager,
            email = email,
            genres = genres,
            workingCity = workingCity,
            workingDistrict = workingDistrict,
            workingStartDate = workingStartDate,
            workingEndDate = workingEndDate,
            selectedDays = selectedDays,
            workingStartTime = workingStartTime,
            workingEndTime = workingEndTime,
            salaryType = salaryType,
            salary = salary
        )
    }
}
