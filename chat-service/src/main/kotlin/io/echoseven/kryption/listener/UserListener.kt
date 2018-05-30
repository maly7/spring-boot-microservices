package io.echoseven.kryption.listener

import io.echoseven.kryption.domain.User
import io.echoseven.kryption.service.QueueService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.mapping.event.AbstractMongoEventListener
import org.springframework.data.mongodb.core.mapping.event.AfterSaveEvent
import org.springframework.stereotype.Component

@Component
class UserListener : AbstractMongoEventListener<User>() {

    @Autowired
    lateinit var queueService: QueueService

    override fun onAfterSave(event: AfterSaveEvent<User>) {
        super.onAfterSave(event)
        queueService.createUserQueue(event.source.id!!)
    }
}
