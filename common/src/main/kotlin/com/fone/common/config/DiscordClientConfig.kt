package com.fone.common.config

import club.minnced.discord.webhook.WebhookClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DiscordClientConfig(
    @Value("\${discord.question}") val questionUrl: String,
    @Value("\${discord.report}") val reportUrl: String,
    @Value("\${discord.monitor}") val monitorUrl: String,
) {
    private val dummyUrl =
        "https://discord.com/api/webhooks/99999990000000/213aWRR5sEeY5UhOk7twvFSDFVC-Feqw"

    @Bean("QuestionWebhook")
    fun discordQuestionClient(): WebhookClient {
        return if (questionUrl.startsWith("https://discord.com")) {
            WebhookClient.withUrl(questionUrl)
        } else {
            WebhookClient.withUrl(dummyUrl)
        }
    }

    @Bean("ReportWebhook")
    fun discordReportClient(): WebhookClient {
        return if (reportUrl.startsWith("https://discord.com")) {
            WebhookClient.withUrl(reportUrl)
        } else {
            WebhookClient.withUrl(dummyUrl)
        }
    }

    @Bean("MonitorWebhook")
    fun monitorReportClient(): WebhookClient {
        return if (monitorUrl.startsWith("https://discord.com")) {
            WebhookClient.withUrl(monitorUrl)
        } else {
            WebhookClient.withUrl(dummyUrl)
        }
    }
}
