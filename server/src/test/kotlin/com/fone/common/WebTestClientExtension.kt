package com.fone.common

import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.util.UriBuilder
import java.net.URI

fun WebTestClient.doGet(url: String, queryParams: Map<String, Any>? = null): WebTestClient.ResponseSpec {
    return this.get().uri() { it.setUriBuilder(url, queryParams) }.exchange()
}

fun <T> WebTestClient.doPost(
    url: String,
    request: T,
    queryParams: Map<String, Any>? = null
): WebTestClient.ResponseSpec {
    return this.post()
        .uri() { it.setUriBuilder(url, queryParams) }
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(request))
        .exchange()
}

fun <T> WebTestClient.doPut(
    url: String,
    request: T,
    queryParams: Map<String, Any>? = null
): WebTestClient.ResponseSpec {
    return this.put()
        .uri() { it.setUriBuilder(url, queryParams) }
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(request))
        .exchange()
}

fun WebTestClient.doDelete(url: String, queryParams: Map<String, Any>? = null): WebTestClient.ResponseSpec {
    return this.delete().uri() { it.setUriBuilder(url, queryParams) }.exchange()
}

private fun UriBuilder.setUriBuilder(url: String, queryParams: Map<String, Any>? = null): URI {
    return this.path(url)
        .run {
            if (queryParams != null) {
                this.queryParams(
                    LinkedMultiValueMap<String, String>().apply {
                        setAll(queryParams.mapValues { it.value.toString() })
                    }
                )
            }
            this
        }
        .build()
}
