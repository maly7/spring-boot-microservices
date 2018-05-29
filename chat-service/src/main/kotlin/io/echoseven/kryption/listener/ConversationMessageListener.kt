package io.echoseven.kryption.listener

import io.echoseven.kryption.domain.ConversationMessage
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener
import org.springframework.data.mongodb.core.mapping.event.BeforeConvertEvent
import org.springframework.stereotype.Component
import java.time.Instant
import java.util.Date

@Component
class ConversationMessageListener : AbstractMongoEventListener<ConversationMessage>() {
    override fun onBeforeConvert(event: BeforeConvertEvent<ConversationMessage>) {
        if (event.source.timestamp == null) {
            event.source.timestamp = Date.from(Instant.now())
        }

        super.onBeforeConvert(event)
    }
}
