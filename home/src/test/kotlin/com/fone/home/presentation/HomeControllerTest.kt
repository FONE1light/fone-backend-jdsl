package com.fone.home.presentation

import com.fone.common.response.CommonResponse
import io.kotest.core.spec.style.FunSpec
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.delay
import kotlinx.coroutines.reactor.awaitSingle
import org.slf4j.LoggerFactory
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

internal class HomeControllerTest : FunSpec() {
    private val logger = LoggerFactory.getLogger(HomeControllerTest::class.java)
    private var count = 1
    private val mockResponse = LinkedHashMap(
        mapOf(
            "nickname" to "nickname",
            "job" to "job",
            "jobOpenings" to "jobOpenings",
            "competitions" to "competitions",
            "profiles" to "profiles"
        )
    )
    //mockkAwaitSingle 활용하지 않기 때문에 불필요해 보일 수 있으나 Mono.awaitSingle StaticMock으로 만들지 않을 경우 webclientMock가 정상적으로 생성되지 않음
    private val mockAwaitSingle = mockkStatic(Mono<*>::awaitSingle)
    private val webclientMock: WebClient = mockk {
        every { get() } returns mockk {
            every { uri(any<String>()) } returns mockk {
                every { header(any(), *anyVararg()) } returns mockk {
                    every { retrieve() } returns mockk {
                        every { bodyToMono(CommonResponse::class.java) } returns mockk{
                            coEvery { awaitSingle() } coAnswers {
                                val mockCount = count++
                                logger.debug("mock IO call #$mockCount starting")
                                delay(1000)
                                logger.debug("mock IO call #$mockCount ended")
                                mockk<CommonResponse<LinkedHashMap<String, String>>> {
                                    every { data } returns mockResponse
                                }
                            }
                        }
                    }
                }
            }
        }
    }


    private val homeController = HomeController(webClient = webclientMock)
    private val homeControllerRefactored = HomeControllerRefactor(webClient = webclientMock)
    init {
        test("suspend 5번 호출됨") {
            homeController.RetrieveHome("")
        }
        test("실제 메소드에서 suspend 두번으로 나눔") {
            homeControllerRefactored.RetrieveHome("")
        }
    }
}
