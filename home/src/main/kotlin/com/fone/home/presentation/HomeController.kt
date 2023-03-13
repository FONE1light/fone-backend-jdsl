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

@Api(tags = ["07. Home Info"], description = "홈 화면 서비스")
@RestController
@RequestMapping("/api/v1/homes")
class HomeController {

    val client = HttpClient.create().responseTimeout(Duration.ofSeconds(1))

    val webClient: WebClient =
        WebClient.builder().clientConnector(ReactorClientHttpConnector(client)).baseUrl("http://localhost:8080").build()

    @GetMapping
    suspend fun RetrieveHome(
        @RequestHeader(value = "Authorization", required = false) token: String,
    ): CommonResponse<HomeDto> {
        val userResponse = webClient.get().uri("/api/v1/users").header("Authorization", token).retrieve()
            .bodyToMono(CommonResponse::class.java)

        val userJob = (userResponse.awaitSingle().data as LinkedHashMap<*, *>)["job"].toString()

        val jobOpeningResponse = webClient.get().uri(
            "/api/v1/job-openings/my-similar?page=0&size=5&sort=viewCount,DESC&type=$userJob"
        ).header("Authorization", token).retrieve().bodyToMono(CommonResponse::class.java)

        val competitionResponse =
            webClient.get().uri("/api/v1/competitions?page=0&size=5&sort=viewCount,DESC").header("Authorization", token)
                .retrieve().bodyToMono(CommonResponse::class.java)

        val profileResponse = webClient.get().uri("/api/v1/profiles?page=0&size=5&sort=createdAt,DESC&type=ACTOR")
            .header("Authorization", token).retrieve().bodyToMono(CommonResponse::class.java)

        val response = HomeDto(
            order = mutableListOf("jobOpening", "competition", "profile").shuffled(),
            jobOpening = CollectionDto(
                title = "나와 비슷한 사람들이 보고있는 공고",
                subTitle = (userResponse.awaitSingle().data as LinkedHashMap<*, *>)["nickname"].toString() +
                    "님 안녕하세요. 관심사 기반으로 꼭 맞는 공고를 추천 합니다.",
                data = (jobOpeningResponse.awaitSingle().data as LinkedHashMap<*, *>)["jobOpenings"]
            ),
            competition = CollectionDto(
                title = "인기 공모전",
                data = (competitionResponse.awaitSingle().data as LinkedHashMap<*, *>)["competitions"]
            ),
            profile = CollectionDto(
                title = "배우 프로필 보기",
                data = (profileResponse.awaitSingle().data as LinkedHashMap<*, *>)["profiles"]
            )
        )

        return CommonResponse.success(response)
    }
}
