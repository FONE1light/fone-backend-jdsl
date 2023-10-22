package com.fone.common.config.log

import kotlinx.coroutines.reactive.awaitFirstOrNull
import kotlinx.coroutines.runBlocking
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.core.io.buffer.DefaultDataBufferFactory
import org.springframework.http.server.reactive.ServerHttpRequestDecorator
import org.springframework.stereotype.Component
import org.springframework.web.server.ServerWebExchange
import org.springframework.web.server.WebFilter
import org.springframework.web.server.WebFilterChain
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

@Component
class BodyBufferWebFilter : WebFilter {
    override fun filter(exchange: ServerWebExchange, chain: WebFilterChain): Mono<Void> {
        val newRequest = runBlocking {
            val buffer = DataBufferUtils.join(exchange.request.body).awaitFirstOrNull()
            if (buffer != null) {
                val bytes = ByteArray(buffer.capacity())
                buffer.read(bytes)
                DataBufferUtils.release(buffer)
                object : ServerHttpRequestDecorator(exchange.request) {
                    override fun getBody() =
                        Flux.just(DefaultDataBufferFactory.sharedInstance.wrap(bytes) as DataBuffer)
                }
            } else {
                exchange.request
            }
        }
        return chain.filter(exchange.mutate().request(newRequest).build())
    }
}
