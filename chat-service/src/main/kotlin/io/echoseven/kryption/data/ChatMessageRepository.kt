package io.echoseven.kryption.data

import io.echoseven.kryption.domain.ChatMessage
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ChatMessageRepository : MongoRepository<ChatMessage, String> {
    fun findAllByFromId(fromId: String): List<ChatMessage>

    fun findAllByToId(toId: String): List<ChatMessage>
}
