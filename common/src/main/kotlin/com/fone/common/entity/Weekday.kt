package com.fone.common.entity

enum class Weekday(name: String) {
    MON("월"),
    TUE("화"),
    WED("수"),
    THU("목"),
    FRI("금"),
    SAT("토"),
    SUN("일"),
    ;

    companion object {
        operator fun invoke(name: String) = Weekday.valueOf(name.uppercase())
    }
}
