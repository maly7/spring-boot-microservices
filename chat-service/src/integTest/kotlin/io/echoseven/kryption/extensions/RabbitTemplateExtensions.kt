package io.echoseven.kryption.extensions

import org.springframework.amqp.rabbit.core.RabbitTemplate

fun RabbitTemplate.receive(queue: String, timeout: Long, count: Int) {
    for (i in 1..count) {
        this.receive(queue, timeout)
    }
}
