package io.echoseven.kryption.web

import io.echoseven.kryption.ChatIntegrationTest
import io.echoseven.kryption.extensions.deleteConversation
import io.echoseven.kryption.extensions.receive
import io.echoseven.kryption.extensions.sendConversationMessage
import io.echoseven.kryption.support.ConversationSupport
import io.echoseven.kryption.support.assertDeleteConversationNotification
import io.echoseven.kryption.support.assertNewConversationNotification
import io.echoseven.kryption.support.assertNewMessageNotification
import io.echoseven.kryption.util.userQueueId
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@ChatIntegrationTest
class ConversationNotificationTests : ConversationSupport() {
    private val queueTimeout = 5000L

    @Autowired
    lateinit var rabbitTemplate: RabbitTemplate

    @Test
    fun `The recipient should receive a notification on new messages`() {
        val messageText = "First Notification Message"
        val conversation = restTemplate.sendConversationMessage(userToken, contact.id!!, messageText).body!!
        val firstMessage = conversation.messages.first()

        val conversationNotification = rabbitTemplate.receive(userQueueId(contact), queueTimeout)
        assertNewConversationNotification(conversationNotification, conversation, currentUser.id!!, contact.id!!)

        val messageNotification = rabbitTemplate.receive(userQueueId(contact), queueTimeout)
        assertNewMessageNotification(messageNotification, conversation, firstMessage)

        val updatedConversation = restTemplate.sendConversationMessage(contactToken, currentUser.id!!, "Reply").body!!
        val replyMessage = updatedConversation.messages.last()

        val replyNotification = rabbitTemplate.receive(userQueueId(currentUser), queueTimeout)
        assertNewMessageNotification(replyNotification, updatedConversation, replyMessage)
    }

    @Test
    fun `Both participants should receive a notification on conversation deletion`() {
        val conversation = restTemplate.sendConversationMessage(userToken, contact.id!!, "test").body!!

        rabbitTemplate.receive(userQueueId(contact), queueTimeout, 2)
        rabbitTemplate.receive(userQueueId(currentUser), queueTimeout, 1)

        restTemplate.deleteConversation(userToken, conversation)

        val senderNotification = rabbitTemplate.receive(userQueueId(currentUser), queueTimeout)
        assertDeleteConversationNotification(senderNotification, conversation)

        val recipientNotification = rabbitTemplate.receive(userQueueId(contact), queueTimeout)
        assertDeleteConversationNotification(recipientNotification, conversation)
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
