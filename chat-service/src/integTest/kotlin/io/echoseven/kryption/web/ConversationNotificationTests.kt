package io.echoseven.kryption.web

import io.echoseven.kryption.ChatIntegrationTest
import io.echoseven.kryption.support.ConversationSupport
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@ChatIntegrationTest
class ConversationNotificationTests : ConversationSupport() {

    @Autowired
    lateinit var rabbitTemplate: RabbitTemplate

    @Test
    fun `The recipient should receive a notification on new messages`() {
    }

    @Test
    fun `Both participants should receive a notification on conversation deletion`() {
    }

    @Test
    fun `Both participants should receive a notification on message deletion`() {
    }

    @Test
    fun `Both participants should receive a conversation deletion notification upon deleting the last message of a conversation`() {
    }

    @Test
    fun `Non-participants should not receive notifications`() {
    }
}