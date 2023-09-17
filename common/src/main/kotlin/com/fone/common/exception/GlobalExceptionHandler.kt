package com.fone.common.exception

import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import com.fone.common.response.CommonResponse
import com.fone.common.response.Error
import com.fone.common.response.ErrorCode
import mu.KotlinLogging
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.FieldError
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.bind.support.WebExchangeBindException
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.ServerWebInputException
import reactor.core.publisher.Mono

@RestControllerAdvice
class GlobalExceptionHandler {

    private val logger = KotlinLogging.logger {}

    @ExceptionHandler(ServerException::class)
    fun handleServerException(ex: ServerException): ResponseEntity<Any> {
        val response = CommonResponse.fail(ex.message, ex.javaClass.simpleName)
        return ResponseEntity(response, null, ex.code)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = [ServerWebInputException::class])
    fun methodArgumentNotValidException(
        e: ServerWebInputException,
        exchange: ServerWebExchange,
    ): Mono<CommonResponse<String?>> {
        println("test..12313")
        val data = if (e.cause?.cause is MissingKotlinParameterException) {
            val param = (e.cause?.cause as MissingKotlinParameterException).parameter.name
            "필드명: $param"
        } else {
            null
        }

        println("Request URI: ${exchange.request.uri}")
        println(data)
        val errorResponse = CommonResponse.fail(data, ErrorCode.COMMON_NULL_PARAMETER)
        return Mono.just(errorResponse)
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(value = [UnauthorizedException::class])
    fun tokenInvalidException(
        e: UnauthorizedException,
    ): Mono<CommonResponse<Nothing>> {
        val errorResponse = CommonResponse.fail(e.message, e::class.java.simpleName)
        return Mono.just(errorResponse)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = [InvalidOauthStatusException::class])
    fun invalidOauthUserException(
        e: InvalidOauthStatusException,
    ): Mono<CommonResponse<Nothing>> {
        val errorResponse = CommonResponse.fail(e.message, e::class.java.simpleName)
        return Mono.just(errorResponse)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = [WebExchangeBindException::class])
    fun methodArgumentNotValidException(e: WebExchangeBindException): Mono<CommonResponse<String>> {
        val errors = mutableListOf<Error>()
        e.allErrors.forEach {
            val error = Error(
                field = (it as FieldError).field,
                message = it.defaultMessage,
                value = it.rejectedValue
            )
            errors.add(error)
        }

        val errorResponse = CommonResponse.fail(errors.toString(), ErrorCode.COMMON_INVALID_PARAMETER)

        return Mono.just(errorResponse)
    }
}
