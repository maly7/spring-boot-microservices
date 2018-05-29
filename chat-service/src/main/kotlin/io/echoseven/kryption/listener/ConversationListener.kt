package io.echoseven.kryption.listener

import io.echoseven.kryption.domain.Conversation
import io.echoseven.kryption.service.NotificationService
import io.echoseven.kryption.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent
import org.springframework.stereotype.Component

@Component
class ConversationListener : AbstractMongoEventListener<Conversation>() {

    @Autowired
    lateinit var notificationService: NotificationService

    @Autowired
    lateinit var userService: UserService

    override fun onAfterSave(event: AfterSaveEvent<Conversation>) {
        super.onAfterSave(event)
        val conversation = event.source

        if (conversation.messages.isEmpty()) {
            val userId = conversation.participants.find { it != userService.getCurrentUserId() }.orEmpty()
            notificationService.notifyNewConversation(userId, conversation)
        }
    }
}
