package io.echoseven.kryption.service

import io.echoseven.kryption.domain.User
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.core.RabbitTemplate
import org.springframework.stereotype.Service

@Service
class NotificationService(val rabbitTemplate: RabbitTemplate) {
    private val log = LoggerFactory.getLogger(NotificationService::class.java)

    fun notifyUser(user: User) {
        rabbitTemplate.convertAndSend("user.updates", "user.${user.id}", "Test Message")
    }
}
