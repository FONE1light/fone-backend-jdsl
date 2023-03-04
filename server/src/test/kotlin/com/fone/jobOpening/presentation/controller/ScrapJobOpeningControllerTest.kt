package com.fone.jobOpening.presentation.controller

import com.fone.common.CustomDescribeSpec
import com.fone.common.IntegrationTest
import org.springframework.test.web.reactive.server.WebTestClient

@IntegrationTest
class ScrapJobOpeningControllerTest(client: WebTestClient) : CustomDescribeSpec()
