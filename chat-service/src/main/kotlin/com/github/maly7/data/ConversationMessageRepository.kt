package com.github.maly7.data

import com.github.maly7.domain.ConversationMessage
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ConversationMessageRepository : MongoRepository<ConversationMessage, String> {
    fun findAllByFromId(fromId: String): List<ConversationMessage>

    fun findAllByToId(toId: String): List<ConversationMessage>
}
