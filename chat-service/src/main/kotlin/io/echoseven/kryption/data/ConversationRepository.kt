package io.echoseven.kryption.data

import io.echoseven.kryption.domain.Conversation
import io.echoseven.kryption.domain.ConversationMessage
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface ConversationRepository : MongoRepository<Conversation, String> {
    fun findByParticipantsContaining(toId: String, fromId: String): Optional<Conversation>

    fun findByIdAndParticipantsContaining(id: String, userId: String): Optional<Conversation>

    fun findByMessagesContaining(message: ConversationMessage): Optional<Conversation>
}
