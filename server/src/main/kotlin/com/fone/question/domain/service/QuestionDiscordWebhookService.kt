package com.fone.question.domain.service

import club.minnced.discord.webhook.WebhookClient
import club.minnced.discord.webhook.send.WebhookEmbed
import club.minnced.discord.webhook.send.WebhookEmbed.EmbedField
import club.minnced.discord.webhook.send.WebhookMessageBuilder
import com.fone.common.exception.NotFoundUserException
import com.fone.question.domain.entity.Question
import com.fone.user.domain.repository.UserRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.future.asDeferred
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactor.asCoroutineDispatcher
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import mu.KLogging
import org.springframework.stereotype.Service
import reactor.core.scheduler.Schedulers
import javax.annotation.PostConstruct

@Service
class QuestionDiscordWebhookService(
    private val webhookClient: WebhookClient,
    private val userRepository: UserRepository,
) {
    private val webhookFlow = MutableSharedFlow<Question>(extraBufferCapacity = 10)

    companion object : KLogging()

    @PostConstruct
    fun webhookFlowConsumer() {
        Schedulers.boundedElastic().schedule {
            runBlocking {
                withContext(Schedulers.boundedElastic().asCoroutineDispatcher()) {
                    webhookFlow.collect {
                        launch {
                            sendQuestion(it)
                        }
                    }
                }
            }
        }
    }

    suspend fun send(question: Question) {
        webhookFlow.emit(question)
    }

    private suspend fun sendQuestion(question: Question) {
        val builder = WebhookMessageBuilder()
        builder.setContent("문의가 들어왔습니다.")
        val embed = WebhookEmbed(
            null,
            null,
            question.description,
            null,
            null,
            null,
            WebhookEmbed.EmbedTitle(question.title, null),
            WebhookEmbed.EmbedAuthor(question.email, null, null),
            parseFields(question)
        )
        builder.addEmbeds(embed)
        val response = webhookClient.send(builder.build()).asDeferred().await()
        logger.info { response }
    }

    private suspend fun parseFields(question: Question): List<EmbedField> {
        val fields = mutableListOf(
            EmbedField(
                true,
                "개인정보 이용 동의",
                question.agreeToPersonalInformation.toString()
            ),
            EmbedField(
                true,
                "종류",
                question.type.toString()
            )
        )
        if (question.userId != null) {
            fields.addUserInfo(question)
        }
        return fields
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
