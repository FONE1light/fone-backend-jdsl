package com.fone.filmone.common.response

enum class ErrorCode(
    val errorMsg: String,
) {
    // 500
    COMMON_SYSTEM_ERROR("일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요."),

    // 400
    COMMON_NULL_PARAMETER("빠뜨린 값이 없는지 확인 해주세요."),
    COMMON_INVALID_PARAMETER("요청한 값이 올바르지 않습니다."),
    ;

    fun getErrorMsg(vararg arg: Any?): String {
        return String.format(errorMsg!!, *arg)
    }
}