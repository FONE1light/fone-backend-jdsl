package com.fone.jobOpening.domain.entity

import com.fone.common.converter.SeparatorConverter
import com.fone.common.entity.Genre
import com.fone.common.entity.Salary
import com.fone.common.entity.Weekday
import java.time.LocalDate
import java.time.LocalTime
import javax.persistence.Column
import javax.persistence.Convert
import javax.persistence.Embeddable

@Embeddable
data class Work(
    @Column(length = 20) var produce: String,
    @Column(length = 20) var workTitle: String,
    @Column(length = 20) var director: String,
    @Column(length = 20)
    @Deprecated("genres 로 대체합니다.")
    var genre: String,
    @Column var logline: String?,
    @Column
    @Deprecated("workingCity, workingDistrict 로 대체합니다.")
    var location: String?,
    @Column
    @Deprecated("workingStartDate, workingEndDate 로 대체합니다.")
    var period: String?,
    @Column
    @Deprecated("salaryType, salary 로 대체합니다.")
    var pay: String?,
    @Column(length = 500) var details: String,
    @Column(length = 10) var manager: String,
    @Column var email: String,

    @Convert(converter = SeparatorConverter::class)
    var genres: List<Genre> = listOf(),
    @Column var workingCity: String,
    @Column var workingDistrict: String,
    @Column var workingStartDate: LocalDate?,
    @Column var workingEndDate: LocalDate?,
    @Convert(converter = SeparatorConverter::class)
    var selectedDays: List<Weekday> = listOf(),
    @Column var workingStartTime: LocalTime?,
    @Column var workingEndTime: LocalTime?,
    @Column var salaryType: Salary,
    @Column var salary: Int,
) {
    fun delete() {
        produce = ""
        workTitle = ""
        director = ""
        genre = ""
        logline = ""
        location = ""
        period = ""
        pay = ""
        details = ""
        manager = ""
        email = ""
    }
}
