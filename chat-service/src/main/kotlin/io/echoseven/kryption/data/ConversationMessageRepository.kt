package io.echoseven.kryption.data

import io.echoseven.kryption.domain.ConversationMessage
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ConversationMessageRepository : MongoRepository<ConversationMessage, String> {
    fun findAllByFromId(fromId: String): List<ConversationMessage>

    fun findAllByToId(toId: String): List<ConversationMessage>
}
