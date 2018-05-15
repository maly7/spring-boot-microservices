package io.echoseven.kryption.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "chats")
data class Chat(
    @DBRef var messages: List<ChatMessage> = listOf(),
    var participants: List<String> = listOf()
) {
    @Id
    var id: String? = null
}
