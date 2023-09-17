package com.fone.common.config.log

import io.vertx.core.impl.logging.LoggerFactory
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Mono
import java.nio.charset.StandardCharsets

@Component
class LoggingWebFilter : WebFilter {
    private val logger = LoggerFactory.getLogger(LoggingWebFilter::class.java)

    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val originalRequestBody = exchange.request.body
        val dataBufferList = mutableListOf<DataBuffer>()

        val cachingRequestBody = originalRequestBody.doOnNext { dataBuffer ->
            val asByteBuffer = dataBuffer.asByteBuffer().asReadOnlyBuffer()
            val byteArr = ByteArray(asByteBuffer.remaining())
            asByteBuffer.get(byteArr)
            dataBufferList.add(exchange.response.bufferFactory().wrap(byteArr))
        }

        return chain.filter(exchange.mutate().request(exchange.request.mutate().build()).build())
            .doOnTerminate {
                val combinedRequestBody = dataBufferList.joinToString(separator = "").byteInputStream().readBytes()
                requestLogging(exchange, String(combinedRequestBody, StandardCharsets.UTF_8))
                // Note: The response logging part here is a bit tricky. Actual response body logging might require a more complex setup.
                responseLogging(exchange)
            }
    }

    private fun requestLogging(exchange: ServerWebExchange, body: String) {
        val method = exchange.request.method
        val path = exchange.request.path.pathWithinApplication().value()
        logger.info("Request : $method $path | $body")
    }

    private fun responseLogging(exchange: ServerWebExchange) {
        val method = exchange.request.method
        val path = exchange.request.path.pathWithinApplication().value()
        // Note: Extracting the actual response body in WebFlux is non-trivial and might require storing the response as well.
        // For simplicity, we are not logging the actual response body here.
        logger.info("Response : $method $path")
    }
}
