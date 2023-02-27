package com.fone.common

import java.net.URI
import org.springframework.http.MediaType
import org.springframework.test.web.reactive.server.WebTestClient
import org.springframework.util.LinkedMultiValueMap
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.util.UriBuilder

fun WebTestClient.doGet(
    url: String,
    token: String?,
    queryParams: Map<String, Any>? = null,
): WebTestClient.ResponseSpec {
    if (token == null) {
        return this.get().uri() { it.setUriBuilder(url, queryParams) }.exchange()
    }

    return this.get()
        .uri() { it.setUriBuilder(url, queryParams) }
        .headers { it.setBearerAuth(token) }
        .exchange()
}

fun <T> WebTestClient.doPost(
    url: String,
    request: T,
    token: String? = null,
    queryParams: Map<String, Any>? = null,
): WebTestClient.ResponseSpec {
    if (request == null && token != null) {
        return this.post()
            .uri() { it.setUriBuilder(url, queryParams) }
            .contentType(MediaType.APPLICATION_JSON)
            .headers { it.setBearerAuth(token) }
            .exchange()
    }

    if (token == null) {
        return this.post()
            .uri() { it.setUriBuilder(url, queryParams) }
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue(request))
            .exchange()
    }

    return this.post()
        .uri() { it.setUriBuilder(url, queryParams) }
        .contentType(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromValue(request))
        .headers { it.setBearerAuth(token) }
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

fun WebTestClient.doDelete(
    url: String,
    queryParams: Map<String, Any>? = null
): WebTestClient.ResponseSpec {
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
