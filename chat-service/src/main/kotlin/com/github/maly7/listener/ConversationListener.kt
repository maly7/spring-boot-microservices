package com.github.maly7.listener

import com.github.maly7.domain.Conversation
import com.github.maly7.service.NotificationService
import com.github.maly7.service.UserService
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
