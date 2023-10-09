package com.fone.common.utils

import java.sql.Date
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.Calendar

object DateTimeFormat {
    private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun calculateDays(deadline: LocalDate?): String {
        if (deadline == null) {
            return "상시 모집"
        }

        val period = Period.between(LocalDate.now(), deadline)
        val totalDays = period.years * 365 + period.months * 30 + period.days

        return if (totalDays >= 0) "D-$totalDays" else "마감"
    }

    fun calculateAge(date: LocalDate?): Int {
        val birthCalendar = Calendar.getInstance()
        birthCalendar.time = Date.valueOf(date.toString())
        val current = Calendar.getInstance()
        val currentYear = current[Calendar.YEAR]
        val currentMonth = current[Calendar.MONTH]
        val currentDay = current[Calendar.DAY_OF_MONTH]
        var age = currentYear - birthCalendar[Calendar.YEAR]
        if (
            birthCalendar[Calendar.MONTH] * 100 + birthCalendar[Calendar.DAY_OF_MONTH] >
            currentMonth * 100 + currentDay
        ) {
            age--
        }

        return age
    }

    fun calculdateLocalDate(age: Int): LocalDate {
        val nowDate = LocalDate.now()

        return nowDate.minusYears(age.toLong())
    }
}
