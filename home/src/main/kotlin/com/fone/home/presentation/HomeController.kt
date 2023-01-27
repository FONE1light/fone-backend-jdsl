package com.fone.home.presentation

import io.swagger.annotations.Api
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Api(tags = ["07. Home Info"], description = "홈 서비스")
@RestController
@RequestMapping("/home/v1/home")
class HomeController {

    @GetMapping
    suspend fun test(): String {
        return "test"
    }
}