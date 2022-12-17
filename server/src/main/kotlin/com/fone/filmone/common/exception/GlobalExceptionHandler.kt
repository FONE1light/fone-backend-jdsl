package com.fone.filmone.common.exception

import com.fone.filmone.common.response.CommonResponse
import com.fone.filmone.common.response.ErrorCode
import mu.KotlinLogging
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    private val logger = KotlinLogging.logger {}

    @ExceptionHandler(ServerException::class)
    fun handleServerException(ex: ServerException): CommonResponse<String> {
        logger.error { ex.message }

        return CommonResponse(data = ex.toString(), errorCode = ErrorCode.COMMON_SYSTEM_ERROR)
    }
}