package io.echoseven.kryption.web

import com.beust.klaxon.Klaxon
import io.echoseven.kryption.ChatIntegrationTest
import io.echoseven.kryption.extensions.sendConversationMessage
import io.echoseven.kryption.notify.Notification
import io.echoseven.kryption.notify.NotificationStatus
import io.echoseven.kryption.service.QueueService
import io.echoseven.kryption.support.ConversationSupport
import io.echoseven.kryption.util.userQueueId
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit4.SpringRunner
import java.io.StringReader
import kotlin.test.assertEquals

@RunWith(SpringRunner::class)
@ChatIntegrationTest
class ConversationNotificationTests : ConversationSupport() {
    private val QUEUE_TIMEOUT = 5000L
    private val klaxon = Klaxon()

    @Autowired
    lateinit var rabbitTemplate: RabbitTemplate

    @Autowired
    lateinit var queueService: QueueService

    @Test
    fun `The recipient should receive a notification on new messages`() {
        val messageText = "First Notification Message"
        val response = restTemplate.sendConversationMessage(userToken, contact.id!!, messageText)
        val conversation = response.body!!
        val firstMessage = conversation.messages.first()

        val message = rabbitTemplate.receive(userQueueId(contact), QUEUE_TIMEOUT)
        assertNotNull("The contact should receive a notification", message)

        val messageBody = String(message.body)
        val notification = klaxon.parse<Notification>(messageBody)!!
        val content = klaxon.parseJsonObject(StringReader(messageBody))["content"] as Map<*, *>

        assertEquals(
            NotificationStatus.NEW_MESSAGE,
            notification.status,
            "The notification should be for a new message"
        )
        assertEquals(conversation.id, notification.conversationId, "The notification should be for this conversation")
        assertEquals(firstMessage.id, notification.messageId, "The notification should be for this message")
        assertEquals(messageText, content["message"], "The message text should be in the notification")
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
