package com.fone.common.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.ses.SesClient

@Configuration
class AwsSesConfig(
    @Value("\${security.aws.accessKey}") val accessKey: String,
    @Value("\${security.aws.secretKey}") val secretKey: String,
) {
    private val awsCred = AwsBasicCredentials.create(accessKey, secretKey)

    @Bean
    fun sesClient(): SesClient = SesClient.builder()
        .region(Region.AP_NORTHEAST_2)
        .credentialsProvider(StaticCredentialsProvider.create(awsCred))
        .build()
}
