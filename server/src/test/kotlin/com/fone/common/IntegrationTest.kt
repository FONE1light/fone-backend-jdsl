package com.fone.common

import com.fone.filmone.ServerApplication
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.junit.jupiter.Testcontainers

@SpringBootTest(classes = [ServerApplication::class])
@Testcontainers
@AutoConfigureWebTestClient
@ContextConfiguration(initializers = [IntegrationTestContextInitializer::class])
annotation class IntegrationTest
