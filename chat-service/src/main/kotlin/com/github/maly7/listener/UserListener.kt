package com.github.maly7.listener

import com.github.maly7.domain.User
import com.github.maly7.service.QueueService
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
