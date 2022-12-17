package com.fone.filmone.common.response

enum class ErrorCode(
    val errorMsg: String,
) {
    COMMON_SYSTEM_ERROR("일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요.")
    ;

    fun getErrorMsg(vararg arg: Any?): String {
        return String.format(errorMsg!!, *arg)
    }
}