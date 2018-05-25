package io.echoseven.kryption.service

import io.echoseven.kryption.domain.User
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

    fun notifyUser(user: User) {
        rabbitTemplate.convertAndSend(userExchange.name, queueService.userQueueId(user.id!!), "Test Message")
    }
}
