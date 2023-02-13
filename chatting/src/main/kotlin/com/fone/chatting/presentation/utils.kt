package com.fone.chatting.presentation

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.PropertyNamingStrategy
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import java.net.URI
import java.net.URLDecoder

fun parseQueryString(url: URI): Map<String, String> {
    val queryPairs = mutableMapOf<String, String>()

    val query: String = url.query
    val pairs = query.split("&")

    for (pair in pairs) {
        val idx = pair.indexOf("=")
        queryPairs[URLDecoder.decode(pair.substring(0, idx), "UTF-8")] =
            URLDecoder.decode(pair.substring(idx + 1), "UTF-8")
    }

    return queryPairs
}

fun jsonMapper(): ObjectMapper =
    jacksonObjectMapper().apply { propertyNamingStrategy = PropertyNamingStrategy.SNAKE_CASE }
