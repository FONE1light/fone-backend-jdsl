package com.fone.common.entity

enum class Salary(name: String) {
    ANNUAL("연봉"),
    MONTHLY("월급"),
    DAILY("일급"),
    HOURLY("시급"),
    PER_SESSION("회차"),
    ;

    companion object {
        operator fun invoke(name: String) = Salary.valueOf(name.uppercase())
    }
}
