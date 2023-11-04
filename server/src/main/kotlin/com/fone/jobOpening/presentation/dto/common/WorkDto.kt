package com.fone.jobOpening.presentation.dto.common

import com.fone.jobOpening.domain.entity.Work
import io.swagger.v3.oas.annotations.media.Schema

data class WorkDto(
    @Schema(description = "제작", example = "FONE")
    val produce: String,
    @Schema(description = "제목", example = "작품 제목")
    val workTitle: String,
    @Schema(description = "연출", example = "이하은")
    val director: String,
    @Schema(description = "장르", example = "스릴러")
    val genre: String,
    @Schema(description = "로그라인", example = "자폐 스펙트럼 장애를 가진 변호사의 이야기")
    val logline: String?,
    @Schema(description = "촬영위치", example = "서울")
    val location: String?,
    @Schema(description = "촬영기간", example = "2021-10-10 ~ 2021-10-11")
    val period: String?,
    @Schema(description = "급여", example = "추후협의")
    val pay: String?,
    @Schema(description = "상세요강", example = "상세내용")
    val details: String,
    @Schema(description = "담당자", example = "김매니저")
    val manager: String,
    @Schema(description = "이메일", example = "test@test.com")
    val email: String,
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
        email = work.email
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
            email = email
        )
    }
}
