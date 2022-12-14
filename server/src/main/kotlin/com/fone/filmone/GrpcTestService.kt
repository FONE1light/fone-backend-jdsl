package com.fone.filmone

import com.fone.filmone.idl.TestServiceGrpcKt
import com.fone.filmone.idl.TestUserRequest
import com.fone.filmone.idl.TestUserResponse
import mu.KotlinLogging
import org.springframework.stereotype.Service

@Service
class GrpcTestService : TestServiceGrpcKt.TestServiceCoroutineImplBase() {

    private val log = KotlinLogging.logger {}

    override suspend fun testUser(request: TestUserRequest): TestUserResponse {
        log.info(request.loginId)

        return TestUserResponse
            .newBuilder()
            .setLoginId(request.loginId)
            .build()
    }
}