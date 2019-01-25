package com.github.maly7.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "conversations")
data class Conversation(
    @DBRef var messages: List<ConversationMessage> = listOf(),
    var participants: List<String> = listOf()
) {
    @Id
    var id: String? = null
}
