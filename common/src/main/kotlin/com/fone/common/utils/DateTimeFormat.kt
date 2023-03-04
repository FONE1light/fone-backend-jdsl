package com.fone.common.utils

import java.sql.Date
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.*

object DateTimeFormat {
    private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun calculateDays(deadline: LocalDate?): String {
        val diffDays =
            Period.between(
                LocalDate.parse(LocalDate.now().toString(), dateFormatter),
                LocalDate.parse(deadline.toString(), dateFormatter),
            ).days

        return if (diffDays >= 0) "D-$diffDays" else "마감"
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

        println("test.." + age + ".." + nowDate.minusYears(age.toLong()))
        return nowDate.minusYears(age.toLong())
    }
}
