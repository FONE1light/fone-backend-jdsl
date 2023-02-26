package com.fone.common

import io.kotest.core.listeners.TestListener
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.core.test.TestCase
import io.kotest.core.test.TestResult
import io.kotest.extensions.time.ConstantNowTestListener
import java.time.LocalDateTime

abstract class CustomDescribeSpec(body: DescribeSpec.() -> Unit = {}) : DescribeSpec(body) {
    override fun listeners(): List<TestListener> =
        listOf(ConstantNowTestListener(LocalDateTime.of(2023, 1, 24, 10, 15, 30)))

    override suspend fun afterEach(testCase: TestCase, result: TestResult) {
        return
    }
}
