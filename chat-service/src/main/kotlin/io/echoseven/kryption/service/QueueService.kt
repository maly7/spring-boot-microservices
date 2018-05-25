package io.echoseven.kryption.service

import io.echoseven.kryption.properties.MessagingProperties
import org.springframework.amqp.core.AmqpAdmin
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.Queue
import org.springframework.stereotype.Service

@Service
class QueueService(val amqpAdmin: AmqpAdmin, val messagingProperties: MessagingProperties) {

    fun createUserQueue(userId: String) {
        val queueId = "user.$userId"
        amqpAdmin.declareQueue(Queue(queueId, true))
        amqpAdmin.declareBinding(
            Binding(
                queueId,
                Binding.DestinationType.QUEUE,
                messagingProperties.userExchange,
                queueId,
                mapOf()
            )
        )
    }
}
