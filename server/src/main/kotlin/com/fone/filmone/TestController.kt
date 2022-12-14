package com.fone.filmone

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Mono
import reactor.kotlin.core.publisher.toMono

@RestController
@RequestMapping("/test")
class TestController {

    @GetMapping("/test")
    fun test(): Mono<String> {
        return "hello-test".toMono()
    }
}