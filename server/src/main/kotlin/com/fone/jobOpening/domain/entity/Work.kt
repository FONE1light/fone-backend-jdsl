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
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Embeddable
data class Work(
    @Column(length = 20) var produce: String,
    @Column(length = 20) var workTitle: String,
    @Column(length = 20) var director: String,
    @Column var logline: String?,
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
    @Enumerated(EnumType.STRING) var salaryType: Salary,
    @Column var salary: Int,
) {
    fun delete() {
        produce = ""
        workTitle = ""
        director = ""
        logline = ""
        details = ""
        manager = ""
        email = ""
    }
}
