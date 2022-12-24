package com.fone.filmone.common.response

data class CommonResponse<T>(
    val result: Result,
    val data: T?,
    val message: String,
    val errorCode: String?,
) {

    companion object {
        //status 200 + success (message가 있을 경우)
        fun <T> success(data: T, message: String): CommonResponse<T> {
            return CommonResponse(
                result = Result.SUCCESS,
                data = data,
                message = message,
                errorCode = null,
            )
        }

        //status 200 + success (message가 없을 경우)
        fun <T> success(data: T): CommonResponse<T> {
            return CommonResponse(
                result = Result.SUCCESS,
                data = data,
                message = "",
                errorCode = null,
            )
        }

        //status 200 + success (data가 없을 경우)
        fun <T> success(): CommonResponse<T> {
            return CommonResponse(
                result = Result.SUCCESS,
                data = null,
                message = "",
                errorCode = null,
            )
        }

        //status 200 + fail
        fun <T> fail(data: T, message: String): CommonResponse<T> {
            return CommonResponse(
                result = Result.FAIL,
                data = data,
                message = message,
                errorCode = null,
            )
        }

        fun <T> fail(message: String): CommonResponse<T> {
            return CommonResponse(
                result = Result.FAIL,
                data = null,
                message = message,
                errorCode = null,
            )
        }
    }


    enum class Result {
        SUCCESS, FAIL
    }
}

data class Error(
    val field: String? = null,
    val message: String? = null,
    val value: Any? = null,
)