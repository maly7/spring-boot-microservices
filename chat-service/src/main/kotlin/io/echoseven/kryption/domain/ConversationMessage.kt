package io.echoseven.kryption.domain

import org.springframework.data.annotation.CreatedBy
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.io.Serializable
import java.util.Date
import javax.validation.constraints.NotBlank

@Document(collection = "conversations")
data class ConversationMessage(
    @NotBlank var message: String? = null,
    @CreatedBy var fromId: String? = null,
    @NotBlank var toId: String? = null,
    @CreatedDate var timestamp: Date? = null
) : Serializable {
    @Id
    var id: String? = null

    fun isParticipant(userId: String) = userId == fromId || userId == toId
}
