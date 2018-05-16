package io.echoseven.kryption.data

import io.echoseven.kryption.domain.Chat
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository
import java.util.Optional

@Repository
interface ChatRepository : MongoRepository<Chat, String> {
    fun findByParticipantsContaining(toId: String, fromId: String): Optional<Chat>

    fun findByIdAndParticipantsContaining(id: String, userId: String): Optional<Chat>
}
