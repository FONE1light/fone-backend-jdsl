package com.fone.common.utils

import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

class DateTimeFormat(
) {

    companion object {
        private val dateFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

        fun calculate(deadline: LocalDate?): String {
            val diffDays = Period.between(
                LocalDate.parse(LocalDate.now().toString(), dateFormatter),
                LocalDate.parse(deadline.toString(), dateFormatter)
            ).days


            return if(diffDays >= 0) "D-$diffDays" else "마감"
        }
    }

}