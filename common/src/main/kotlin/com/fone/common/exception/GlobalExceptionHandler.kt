package com.fone.common.exception

import club.minnced.discord.webhook.WebhookClient
import club.minnced.discord.webhook.send.WebhookEmbed
import club.minnced.discord.webhook.send.WebhookMessageBuilder
import com.fasterxml.jackson.module.kotlin.MissingKotlinParameterException
import com.fone.common.response.CommonResponse
import com.fone.common.response.Error
import com.fone.common.response.ErrorCode
import kotlinx.coroutines.reactive.awaitFirstOrDefault
import kotlinx.coroutines.runBlocking
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.core.io.buffer.DefaultDataBufferFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.server.reactive.ServerHttpRequest
import org.springframework.validation.FieldError
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.bind.support.WebExchangeBindException
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.ServerWebInputException
import reactor.core.publisher.Mono

@RestControllerAdvice
class GlobalExceptionHandler(
    @Qualifier("MonitorWebhook")
    private val webhookClient: WebhookClient,
) {

    private val logger = KotlinLogging.logger {}

    @ExceptionHandler(ServerException::class)
    fun handleServerException(ex: ServerException, exchange: ServerWebExchange): ResponseEntity<Any> {
        val embed = WebhookEmbed(
            null,
            null,
            "code: " + ex.code + "\n" + ex.javaClass.simpleName,
            null,
            null,
            null,
            WebhookEmbed.EmbedTitle(exchange.request.uri.toString(), null),
            null,
            emptyList()
        )
        val builder = WebhookMessageBuilder()
        builder.setContent(ex.message)
        builder.addEmbeds(embed)
        builder.addEmbeds(exchange.request.parseBodyToEmbed())
        webhookClient.send(builder.build())

        val response = CommonResponse.fail(ex.message, ex.javaClass.simpleName)
        return ResponseEntity(response, null, ex.code)
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(value = [ServerWebInputException::class])
    fun methodArgumentNotValidException(
        e: ServerWebInputException,
        exchange: ServerWebExchange,
    ): Mono<CommonResponse<String?>> {
        val data = if (e.cause?.cause is MissingKotlinParameterException) {
            val param = (e.cause?.cause as MissingKotlinParameterException).parameter.name
            "항목 ${param}을 확인해주세요"
        } else {
            null
        }

        val embed = WebhookEmbed(
            null,
            null,
            data,
            null,
            null,
            null,
            WebhookEmbed.EmbedTitle(exchange.request.uri.toString(), null),
            null,
            emptyList()
        )
        val builder = WebhookMessageBuilder()
        builder.setContent(ErrorCode.COMMON_NULL_PARAMETER.errorMsg)
        builder.addEmbeds(embed)
        builder.addEmbeds(exchange.request.parseBodyToEmbed())
        webhookClient.send(builder.build())

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
    fun methodArgumentNotValidException(
        e: WebExchangeBindException,
        exchange: ServerWebExchange,
    ): Mono<CommonResponse<String>> {
        val errors = mutableListOf<Error>()
        e.allErrors.forEach {
            val error = Error(
                field = (it as FieldError).field,
                message = it.defaultMessage,
                value = it.rejectedValue
            )
            errors.add(error)
        }

        val embed = WebhookEmbed(
            null,
            null,
            errors.toString(),
            null,
            null,
            null,
            WebhookEmbed.EmbedTitle(exchange.request.uri.toString(), null),
            null,
            emptyList()
        )
        val builder = WebhookMessageBuilder()
        builder.setContent(ErrorCode.COMMON_INVALID_PARAMETER.errorMsg)
        builder.addEmbeds(embed)
        builder.addEmbeds(exchange.request.parseBodyToEmbed())
        webhookClient.send(builder.build())

        val errorResponse = CommonResponse
            .fail(errors.toString(), ErrorCode.COMMON_INVALID_PARAMETER)

        return Mono.just(errorResponse)
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = [Exception::class])
    fun exception(
        e: Exception,
        exchange: ServerWebExchange,
    ): Mono<CommonResponse<String?>> {
        val embed = WebhookEmbed(
            null,
            null,
            e.message + "/" + e::class.java.simpleName,
            null,
            null,
            null,
            WebhookEmbed.EmbedTitle(exchange.request.uri.toString(), null),
            null,
            emptyList()
        )
        val builder = WebhookMessageBuilder()
        builder.setContent("서버에서 정의하지 않은 서버입니다.")
        builder.addEmbeds(embed)
        builder.addEmbeds(exchange.request.parseBodyToEmbed())
        webhookClient.send(builder.build())

        val errorResponse = CommonResponse.fail(e.message, e::class.java.simpleName)
        return Mono.just(errorResponse)
    }

    private fun ServerHttpRequest.parseBodyToEmbed(): WebhookEmbed {
        return WebhookEmbed(
            null,
            null,
            parseBody(),
            null,
            null,
            null,
            WebhookEmbed.EmbedTitle("Body", null),
            null,
            emptyList()
        )
    }
    private fun ServerHttpRequest.parseBody(): String {
        return runBlocking {
            val body = DataBufferUtils.join(this@parseBody.body) // Body 없는 경우 empty string
                .awaitFirstOrDefault(DefaultDataBufferFactory.sharedInstance.wrap("".toByteArray()))
            val bytes = ByteArray(body.capacity())
            body.read(bytes)
            String(bytes)
        }
    }
}
