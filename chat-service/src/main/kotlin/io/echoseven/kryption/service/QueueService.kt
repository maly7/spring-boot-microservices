package io.echoseven.kryption.service

import org.springframework.amqp.core.AmqpAdmin
import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.Exchange
import org.springframework.amqp.core.Queue
import org.springframework.stereotype.Service

@Service
class QueueService(val amqpAdmin: AmqpAdmin, val userExchange: Exchange) {

    fun createUserQueue(userId: String) {
        val queueId = userQueueId(userId)
        amqpAdmin.declareQueue(Queue(queueId, true))
        amqpAdmin.declareBinding(
            Binding(
                queueId,
                Binding.DestinationType.QUEUE,
                userExchange.name,
                queueId,
                mapOf()
            )
        )
    }

    fun userQueueId(id: String) = "user.$id"
}
