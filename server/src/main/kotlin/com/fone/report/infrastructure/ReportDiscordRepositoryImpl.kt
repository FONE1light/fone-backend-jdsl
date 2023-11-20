package com.fone.report.infrastructure

import club.minnced.discord.webhook.WebhookClient
import club.minnced.discord.webhook.send.WebhookEmbed
import club.minnced.discord.webhook.send.WebhookEmbed.EmbedField
import club.minnced.discord.webhook.send.WebhookMessageBuilder
import com.fone.report.domain.entity.Report
import com.fone.report.domain.enum.Type
import com.fone.report.domain.repository.ReportDiscordRepository
import com.fone.user.domain.entity.User
import com.fone.user.domain.repository.UserRepository
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.future.asDeferred
import kotlinx.coroutines.launch
import mu.KotlinLogging
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Repository
import javax.annotation.PostConstruct

@Repository
class ReportDiscordRepositoryImpl(
    @Qualifier("ReportWebhook")
    private val webhookClient: WebhookClient,
    private val userRepository: UserRepository,
) : ReportDiscordRepository {
    private val webhookFlow = MutableSharedFlow<Report>(extraBufferCapacity = 10)

    companion object {
        private val log = KotlinLogging.logger { }
    }

    @OptIn(DelicateCoroutinesApi::class)
    @PostConstruct
    fun webhookFlowConsumer() {
        GlobalScope.launch(Dispatchers.IO) {
            webhookFlow.collect {
                launch {
                    sendReport(it)
                }
            }
        }
    }

    override suspend fun send(report: Report) {
        webhookFlow.emit(report)
    }

    private suspend fun sendReport(report: Report) {
        val builder = WebhookMessageBuilder()
        builder.setContent("신고가 들어왔습니다.")
        val user = userRepository.findById(report.userId)
        val embed = WebhookEmbed(
            null,
            null,
            report.details,
            null,
            null,
            null,
            WebhookEmbed.EmbedTitle(resolveTypeName(report.type), null),
            WebhookEmbed.EmbedAuthor(user!!.email, null, null),

            parseFields(user, report)
        )
        builder.addEmbeds(embed)
        val response = webhookClient.send(builder.build()).asDeferred().await()
        log.info { response }
    }

    private fun resolveTypeName(type: Type): String {
        return when (type) {
            Type.JOB_OPENING -> "모집글"
            Type.CHATTING -> "채팅"
            Type.PROFILE -> "프로필"
        }
    }

    private suspend fun parseFields(user: User, report: Report): List<EmbedField> {
        val inconveniences = EmbedField(
            false,
            "불편사항",
            report.inconvenients.joinToString(separator = "\n") {
                "* $it"
            }
        )
        val userIds = mutableListOf(
            EmbedField(
                true,
                "회원 닉네임",
                user.nickname
            ),
            EmbedField(
                true,
                "신고자 Id",
                report.userId.toString()
            ),
            EmbedField(
                true,
                "대상자 Id",
                report.reportUserId.toString()
            )
        )
        return userIds + inconveniences
    }
}
