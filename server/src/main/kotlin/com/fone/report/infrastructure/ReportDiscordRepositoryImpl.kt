package com.fone.report.infrastructure

import club.minnced.discord.webhook.WebhookClient
import club.minnced.discord.webhook.send.WebhookEmbed
import club.minnced.discord.webhook.send.WebhookEmbed.EmbedField
import club.minnced.discord.webhook.send.WebhookMessageBuilder
import com.fone.common.exception.NotFoundUserException
import com.fone.question.domain.entity.Question
import com.fone.report.domain.entity.Report
import com.fone.report.domain.enum.Type
import com.fone.report.domain.repository.ReportDiscordRepository
import com.fone.user.domain.repository.UserRepository
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.future.asDeferred
import kotlinx.coroutines.launch
import mu.KLogging
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

    companion object : KLogging()

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
            parseFields(report)
        )
        builder.addEmbeds(embed)
        val response = webhookClient.send(builder.build()).asDeferred().await()
        logger.info { response }
    }

    private fun resolveTypeName(type: Type): String {
        return when (type) {
            Type.JOB_OPENING -> "구인구직"
            Type.CHATTING -> "채팅"
            Type.PROFILE -> "프로필"
        }
    }

    private suspend fun parseFields(report: Report): List<EmbedField> {
        val inconveniences = report.inconvenients.map {
            EmbedField(
                true,
                "불편사항",
                it
            )
        }
        val userIds = mutableListOf(
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

    private suspend fun MutableList<EmbedField>.addUserInfo(question: Question) {
        val user = userRepository.findById(question.userId!!) ?: throw NotFoundUserException()
        this.add(
            EmbedField(
                true,
                "회원 이메일",
                user.email
            )
        )
        this.add(
            EmbedField(
                true,
                "회원 닉네임",
                user.nickname
            )
        )
        this.add(
            EmbedField(
                true,
                "회원 No",
                user.id!!.toString()
            )
        )
    }
}
