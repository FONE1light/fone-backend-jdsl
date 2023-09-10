package com.fone.common.config

import club.minnced.discord.webhook.WebhookClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class DiscordClientConfig(@Value("\${discord.question}") val questionUrl: String) {
    @Bean
    fun discordQuestionClient() = WebhookClient.withUrl(questionUrl)
}
