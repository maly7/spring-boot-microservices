package io.echoseven.kryption.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(prefix = "chat.messaging")
data class MessagingProperties(var userExchange: String? = null)

