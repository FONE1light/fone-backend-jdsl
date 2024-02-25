package com.fone.jobOpening.infrastructure

import com.fasterxml.jackson.databind.ObjectMapper
import com.fone.common.CommonJobOpeningCallApi
import com.fone.common.CommonUserCallApi
import com.fone.common.CustomDescribeSpec
import com.fone.common.IntegrationTest
import com.fone.common.entity.Type
import com.fone.jobOpening.domain.repository.JobOpeningRepository
import com.fone.jobOpening.presentation.dto.RetrieveJobOpeningsRequest
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.test.web.reactive.server.WebTestClient

@IntegrationTest
class JobOpeningRepositoryImplTest(
    client: WebTestClient,
    private val objectMapper: ObjectMapper,
    private val jobOpeningRepository: JobOpeningRepository,
) :
    CustomDescribeSpec() {

    init {
        describe("#findByFilters") {
            context("빈 구인구직 리스트를 조회하면") {
                it("성공한다") {
                    val jobOpenings = jobOpeningRepository.findByFilters(
                        org.springframework.data.domain.Pageable.unpaged(),
                        RetrieveJobOpeningsRequest(type = Type.ACTOR)
                    )

                    jobOpenings.size shouldBe 0
                }
            }
        }

        describe("#findById") {
            context("존재하는 구인구직을 상세 조회하면") {
                val (accessToken, _) = CommonUserCallApi.getAccessToken(client)
                val jobOpeningId = CommonJobOpeningCallApi.register(client, accessToken)

                it("성공한다") {
                    val jobOpening = jobOpeningRepository.findById(1)
                    jobOpening shouldNotBe null
                    jobOpening?.id shouldBe 1
                }
            }
        }
    }
}
