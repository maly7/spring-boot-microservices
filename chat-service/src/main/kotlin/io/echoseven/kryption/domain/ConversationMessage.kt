package io.echoseven.kryption.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.io.Serializable
import java.util.Date
import javax.validation.constraints.NotBlank

@Document(collection = "conversations")
data class ConversationMessage(
    @NotBlank var message: String? = null,
    var fromId: String? = null,
    @NotBlank var toId: String? = null,
    var timestamp: Date? = null,
    var imageHeight: Int? = null,
    var imageWidth: Int? = null,
    var imageUrl: String? = null
) : Serializable {
    @Id
    var id: String? = null

    fun isParticipant(userId: String) = userId == fromId || userId == toId
}
