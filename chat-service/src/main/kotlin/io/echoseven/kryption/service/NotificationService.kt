package io.echoseven.kryption.service

import io.echoseven.kryption.domain.ConversationMessage
import io.echoseven.kryption.domain.User
import io.echoseven.kryption.notify.Notification
import io.echoseven.kryption.notify.NotificationStatus
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

    fun notifyNewMessage(user: User, conversationId: String, message: ConversationMessage) {
        log.debug("Notifying user [{}] of new message in conversation [{}]", user.id, conversationId)
        notifyUser(user, Notification(NotificationStatus.NEW_MESSAGE, conversationId, message.id, message))
    }

    fun notifyMessageDelete(users: List<User>, conversationId: String, messageId: String) {
        val notification = Notification(NotificationStatus.DELETE_MESSAGE, conversationId, messageId, "")
        users.forEach { user ->
            log.debug(
                "Notifying user [{}] of message [{}] deletion in conversation [{}]",
                user.id,
                conversationId,
                messageId
            )
            notifyUser(user, notification)
        }
    }

    fun notifyConversationDelete(users: List<User>, conversationId: String) {
        val notification = Notification(NotificationStatus.DELETE_CONVERSATION, conversationId, "", "")
        users.forEach { user ->
            log.debug("Notifying user [{}] of conversation [{}] deletion", user.id, conversationId)
            notifyUser(user, notification)
        }
    }

    private fun notifyUser(user: User, notification: Notification) {
        rabbitTemplate.convertAndSend(userExchange.name, queueService.userQueueId(user), notification)
    }

    fun notifyUser(user: User) {
        rabbitTemplate.convertAndSend(userExchange.name, queueService.userQueueId(user), "Test Message")
    }
}
