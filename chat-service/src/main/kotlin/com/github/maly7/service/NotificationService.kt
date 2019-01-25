package com.github.maly7.service

import com.github.maly7.domain.Conversation
import com.github.maly7.domain.ConversationMessage
import com.github.maly7.notify.Notification
import com.github.maly7.notify.NotificationStatus
import com.github.maly7.util.userQueueId
import org.slf4j.LoggerFactory
import org.springframework.amqp.core.Exchange
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service

@Service
class NotificationService(
    val rabbitTemplate: RabbitTemplate,
    val userExchange: Exchange,
    val queueService: QueueService
) {
    private val log = LoggerFactory.getLogger(NotificationService::class.java)

    fun notifyNewMessage(userId: String, conversationId: String, message: ConversationMessage) {
        log.debug(
            "Notifying user [{}] of new message in conversation [{}]", userId
            , conversationId
        )
        notifyUser(userId, Notification(NotificationStatus.NEW_MESSAGE, conversationId, message.id, message))
    }

    fun notifyNewConversation(userId: String, conversation: Conversation) {
        log.debug("Notifying user [{}] of new conversation [{}]", userId, conversation.id)
        notifyUser(userId, Notification(NotificationStatus.NEW_CONVERSATION, conversation.id, "", conversation))
    }

    fun notifyMessageDelete(users: List<String>, conversationId: String, messageId: String) {
        val notification = Notification(NotificationStatus.DELETE_MESSAGE, conversationId, messageId, "")
        users.forEach { id ->
            log.debug(
                "Notifying user [{}] of message [{}] deletion in conversation [{}]",
                id,
                conversationId,
                messageId
            )
            notifyUser(id, notification)
        }
    }

    fun notifyConversationDelete(users: List<String>, conversationId: String) {
        val notification = Notification(NotificationStatus.DELETE_CONVERSATION, conversationId, "", "")
        users.forEach { id ->
            log.debug("Notifying user [{}] of conversation [{}] deletion", id, conversationId)
            notifyUser(id, notification)
        }
    }

    private fun notifyUser(userId: String, notification: Notification) {
        if (userId.isEmpty() || notification.conversationId.isNullOrEmpty()) {
            log.error("Attempted to notify a blank user id or conversation id, this should never happen. Aborting Notification sending.")
            return
        }
        rabbitTemplate.convertAndSend(userExchange.name, userQueueId(userId), notification)
    }
}
