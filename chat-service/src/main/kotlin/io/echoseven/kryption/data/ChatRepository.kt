package io.echoseven.kryption.data

import io.echoseven.kryption.domain.Chat
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Repository

@Repository
interface ChatRepository : MongoRepository<Chat, String>
