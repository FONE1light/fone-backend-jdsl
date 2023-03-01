package com.fone.jobOpening.presentation.controller

import com.fone.common.CustomDescribeSpec
import com.fone.common.IntegrationTest
import org.junit.jupiter.api.Assertions.*
import org.springframework.test.web.reactive.server.WebTestClient

@IntegrationTest
class DeleteJobOpeningControllerTest(client: WebTestClient) : CustomDescribeSpec() {

    private val deleteUrl = "/api/v1/job-openings"
}
