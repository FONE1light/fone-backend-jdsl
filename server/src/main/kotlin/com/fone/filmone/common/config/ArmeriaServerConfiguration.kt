package com.fone.filmone.common.config

import com.fone.filmone.GrpcTestService
import com.linecorp.armeria.common.grpc.GrpcSerializationFormats
import com.linecorp.armeria.server.docs.DocService
import com.linecorp.armeria.server.grpc.GrpcService
import com.linecorp.armeria.server.logging.AccessLogWriter
import com.linecorp.armeria.server.logging.ContentPreviewingService
import com.linecorp.armeria.server.logging.LoggingService
import com.linecorp.armeria.spring.ArmeriaServerConfigurator
import lombok.RequiredArgsConstructor
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import java.nio.charset.StandardCharsets

@Configuration
@RequiredArgsConstructor
class ArmeriaServerConfiguration(
    val grpcTestService: GrpcTestService
) {

    @Bean
    fun armeriaServerConfigurator(): ArmeriaServerConfigurator? {
        return ArmeriaServerConfigurator { serverBuilder ->
            serverBuilder.decorator(LoggingService.newDecorator())
            serverBuilder.decorator(
                ContentPreviewingService.newDecorator(
                    Int.MAX_VALUE,
                    StandardCharsets.UTF_8
                )
            )
            serverBuilder.accessLogWriter(AccessLogWriter.combined(), false)
            serverBuilder.service(
                GrpcService.builder()
                    .addService(grpcTestService)
                    .supportedSerializationFormats(GrpcSerializationFormats.values())
                    .enableUnframedRequests(true)
                    .build()
            )
            serverBuilder.serviceUnder("/docs", DocService())
        }
    }
}