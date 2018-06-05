package io.echoseven.kryption.web.resource.amqp

data class AmqpUser(val username: String = "", val password: String = "", val tags: List<String> = listOf())
