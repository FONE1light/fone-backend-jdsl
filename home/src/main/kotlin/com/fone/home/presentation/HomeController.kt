package com.fone.home.presentation

import com.fone.common.response.CommonResponse
import io.swagger.annotations.Api
import kotlinx.coroutines.reactor.awaitSingle
import org.springframework.http.client.reactive.ReactorClientHttpConnector
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.reactive.function.client.WebClient
import reactor.netty.http.client.HttpClient
import java.time.Duration

@Api(tags = ["07. Home Info"], description = "홈 서비스")
@RestController
@RequestMapping("/home/v1/home")
class HomeController {

    val client = HttpClient.create()
        .responseTimeout(Duration.ofSeconds(1))

    val webClient: WebClient = WebClient.builder()
        .clientConnector(ReactorClientHttpConnector(client))
        .baseUrl("http://localhost:8080")
        .build()

    @GetMapping
    suspend fun test(
        @RequestHeader(
            value = "Authorization",
            required = false
        ) token: String,
    ): CommonResponse<HomeDto> {
        val userResponse = webClient.get()
            .uri("/api/v1/user")
            .header("Authorization", token)
            .retrieve()
            .bodyToMono(CommonResponse::class.java)

        val jobOpeningResponse = webClient.get()
            .uri("/api/v1/job-openings/my-similar")
            .header("Authorization", token)
            .retrieve()
            .bodyToMono(CommonResponse::class.java)

        val competitionResponse = webClient.get()
            .uri("/api/v1/competitions")
            .header("Authorization", token)
            .retrieve()
            .bodyToMono(CommonResponse::class.java)

        val profileResponse = webClient.get()
            .uri("/api/v1/profiles?type=ACTOR")
            .header("Authorization", token)
            .retrieve()
            .bodyToMono(CommonResponse::class.java)

        val response = HomeDto(
            order = listOf("jobOpenings", "competitions", "profiles"),
            user = userResponse.awaitSingle().data,
            jobOpenings = (jobOpeningResponse.awaitSingle().data as LinkedHashMap<*, *>)["jobOpenings"],
            competitions = (competitionResponse.awaitSingle().data as LinkedHashMap<*, *>)["competitions"],
            profiles = (profileResponse.awaitSingle().data as LinkedHashMap<*, *>)["profiles"],
        )

        return CommonResponse.success(response)
    }
}