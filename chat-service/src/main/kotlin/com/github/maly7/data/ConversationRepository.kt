package com.github.maly7.data

import com.github.maly7.domain.Conversation
import com.github.maly7.domain.ConversationMessage
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface ConversationRepository : MongoRepository<Conversation, String> {
    fun findByParticipantsContaining(toId: String, fromId: String): Optional<Conversation>

    fun findByIdAndParticipantsContaining(id: String, userId: String): Optional<Conversation>

    fun findByMessagesContaining(message: ConversationMessage): Optional<Conversation>
}
