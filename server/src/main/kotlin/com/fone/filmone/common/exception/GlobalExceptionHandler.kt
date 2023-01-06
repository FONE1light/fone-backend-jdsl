package com.fone.filmone.common.exception

import com.fone.filmone.common.response.CommonResponse
import com.fone.filmone.common.response.ErrorCode
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.validation.FieldError
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.bind.support.WebExchangeBindException
import org.springframework.web.server.ServerWebInputException
import reactor.core.publisher.Mono

@RestControllerAdvice
class GlobalExceptionHandler {

    private val logger = KotlinLogging.logger {}

    @ExceptionHandler(ServerException::class)
    fun handleServerException(ex: ServerException): CommonResponse<String> {
        logger.error { ex.message }

        return CommonResponse.fail(ex.toString(), ErrorCode.COMMON_SYSTEM_ERROR.toString())
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = [ServerWebInputException::class])
    fun methodArgumentNotValidException(e: ServerWebInputException): Mono<CommonResponse<Nothing?>> {

        val errorResponse = CommonResponse.fail(
            null,
            ErrorCode.COMMON_NULL_PARAMETER
        )

        return Mono.just(errorResponse)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = [WebExchangeBindException::class])
    fun methodArgumentNotValidException(e: WebExchangeBindException): Mono<CommonResponse<String>> {
        val errors = mutableListOf<com.fone.filmone.common.response.Error>()
        e.allErrors.forEach {
            val error = com.fone.filmone.common.response.Error(
                field = (it as FieldError).field,
                message = it.defaultMessage,
                value = it.rejectedValue,
            )
            errors.add(error)
        }

        val errorResponse = CommonResponse.fail(
            errors.toString(),
            ErrorCode.COMMON_INVALID_PARAMETER
        )

        return Mono.just(errorResponse)
    }
}