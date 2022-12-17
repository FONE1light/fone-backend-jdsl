package com.fone.filmone.common.response

data class CommonResponse<T>(
    val result: Result,
    val data: T?,
    val message: String,
    val errorCode: String?,
) {
    constructor(data: T, message: String) : this(Result.SUCCESS, data, message, null)
    constructor(data: T) : this(Result.SUCCESS, data, "", null)
    constructor(data: T, errorCode: ErrorCode) : this(
        Result.FAIL,
        data,
        errorCode.errorMsg,
        errorCode.name
    )

    enum class Result {
        SUCCESS, FAIL;
    }
}

data class Error(
    val field: String? = null,
    val message: String? = null,
    val value: Any? = null,
)