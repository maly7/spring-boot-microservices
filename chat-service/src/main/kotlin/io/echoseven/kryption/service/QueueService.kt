package io.echoseven.kryption.service

import io.echoseven.kryption.domain.User
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

    fun userQueueId(user: User): String {
        val id = user.id ?: throw IllegalArgumentException("A User cannot have a null id")
        return userQueueId(id)
    }

    fun userQueueId(id: String) = "user.$id"
}
