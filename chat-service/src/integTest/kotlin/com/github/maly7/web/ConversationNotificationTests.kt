package com.github.maly7.web

import com.github.maly7.ChatIntegrationTest
import com.github.maly7.domain.Conversation
import com.github.maly7.extensions.deleteConversation
import com.github.maly7.extensions.deleteMessage
import com.github.maly7.extensions.emptyQueue
import com.github.maly7.extensions.receive
import com.github.maly7.extensions.sendConversationMessage
import com.github.maly7.support.ConversationSupport
import com.github.maly7.support.assertDeleteConversationNotification
import com.github.maly7.support.assertDeleteMessageNotification
import com.github.maly7.support.assertNewConversationNotification
import com.github.maly7.support.assertNewMessageNotification
import com.github.maly7.util.userQueueId
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.test.context.junit4.SpringRunner
import kotlin.test.assertNull

@RunWith(SpringRunner::class)
@ChatIntegrationTest
class ConversationNotificationTests : ConversationSupport() {
    private val queueTimeout = 5000L

    @Autowired
    lateinit var rabbitTemplate: RabbitTemplate

    @Before
    override fun setup() {
        super.setup()

        userRepository.findAll().forEach {
            rabbitTemplate.emptyQueue(userQueueId(it))
        }
    }

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
        val conversation = participateInConversation()
        restTemplate.deleteConversation(userToken, conversation)

        val senderNotification = rabbitTemplate.receive(userQueueId(currentUser), queueTimeout)
        assertDeleteConversationNotification(senderNotification, conversation)

        val recipientNotification = rabbitTemplate.receive(userQueueId(contact), queueTimeout)
        assertDeleteConversationNotification(recipientNotification, conversation)
    }

    @Test
    fun `Both participants should receive a notification on message deletion`() {
        participateInConversation()
        val conversation = participateInConversation()
        val message = conversation.messages.last()
        restTemplate.deleteMessage(contactToken, message)

        val senderNotification = rabbitTemplate.receive(userQueueId(currentUser), queueTimeout)
        assertDeleteMessageNotification(senderNotification, conversation, message)

        val recipientNotification = rabbitTemplate.receive(userQueueId(contact), queueTimeout)
        assertDeleteMessageNotification(recipientNotification, conversation, message)
    }

    @Test
    fun `Both participants should receive a conversation deletion notification upon deleting the last message of a conversation`() {
        val conversation = participateInConversation()
        val message = conversation.messages.last()
        restTemplate.deleteMessage(contactToken, message)

        val senderNotification = rabbitTemplate.receive(userQueueId(currentUser), queueTimeout)
        assertDeleteMessageNotification(senderNotification, conversation, message)

        val recipientNotification = rabbitTemplate.receive(userQueueId(contact), queueTimeout)
        assertDeleteMessageNotification(recipientNotification, conversation, message)

        val senderConversationNotification = rabbitTemplate.receive(userQueueId(currentUser), queueTimeout)
        assertDeleteConversationNotification(senderConversationNotification, conversation)

        val recipientConversationNotification = rabbitTemplate.receive(userQueueId(contact), queueTimeout)
        assertDeleteConversationNotification(recipientConversationNotification, conversation)
    }

    @Test
    fun `Non-participants should not receive notifications`() {
        participateInConversation()

        val message = rabbitTemplate.receive(userQueueId(otherUser), queueTimeout)
        assertNull(message, "Non-participants should not receive notifications")
    }

    private fun participateInConversation(): Conversation {
        val conversation = restTemplate.sendConversationMessage(userToken, contact.id!!, "test").body!!

        rabbitTemplate.receive(userQueueId(contact), queueTimeout, 2)
        rabbitTemplate.receive(userQueueId(currentUser), queueTimeout, 1)

        return conversation
    }
}
