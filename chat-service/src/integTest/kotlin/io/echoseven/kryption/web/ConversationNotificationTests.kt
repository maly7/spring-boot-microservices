package io.echoseven.kryption.web

import com.beust.klaxon.Klaxon
import io.echoseven.kryption.ChatIntegrationTest
import io.echoseven.kryption.extensions.sendConversationMessage
import io.echoseven.kryption.notify.Notification
import io.echoseven.kryption.notify.NotificationStatus
import io.echoseven.kryption.support.ConversationSupport
import io.echoseven.kryption.util.userQueueId
import org.hamcrest.CoreMatchers.hasItem
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit4.SpringRunner
import java.io.StringReader
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

@RunWith(SpringRunner::class)
@ChatIntegrationTest
class ConversationNotificationTests : ConversationSupport() {
    private val QUEUE_TIMEOUT = 5000L
    private val klaxon = Klaxon()

    @Autowired
    lateinit var rabbitTemplate: RabbitTemplate

    @Test
    fun `The recipient should receive a notification on new messages`() {
        val messageText = "First Notification Message"
        val conversation = restTemplate.sendConversationMessage(userToken, contact.id!!, messageText).body!!
        val firstMessage = conversation.messages.first()

        val conversationNotification = rabbitTemplate.receive(userQueueId(contact), QUEUE_TIMEOUT)
        assertNotNull(
            conversationNotification,
            "The contact should first receive a conversation notification"
        )

        var messageBody = String(conversationNotification.body)
        var notification = klaxon.parse<Notification>(messageBody)!!
        var content = klaxon.parseJsonObject(StringReader(messageBody))["content"] as Map<*, *>
        val participants = content["participants"] as List<*>
        assertEquals(
            NotificationStatus.NEW_CONVERSATION,
            notification.status,
            "The first notification should be for a new conversation"
        )
        assertEquals(conversation.id, notification.conversationId, "The notification should be for this conversation")
        assertEquals(conversation.id, content["id"], "The notification content is should be the conversation id")
        assertTrue(
            notification.messageId.isNullOrEmpty(),
            "There should be no message associated with this notification"
        )
        assertThat(
            "The current user is a participant of the conversation from the notification",
            participants,
            hasItem(currentUser.id)
        )
        assertThat(
            "The contact is a participant in the conversation from the notification",
            participants,
            hasItem(contact.id)
        )

        val messageNotification = rabbitTemplate.receive(userQueueId(contact), QUEUE_TIMEOUT)
        assertNotNull(messageNotification, "The contact should receive a notification")

        messageBody = String(messageNotification.body)
        notification = klaxon.parse<Notification>(messageBody)!!
        content = klaxon.parseJsonObject(StringReader(messageBody))["content"] as Map<*, *>

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
