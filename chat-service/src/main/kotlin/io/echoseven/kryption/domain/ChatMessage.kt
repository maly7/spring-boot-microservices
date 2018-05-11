package io.echoseven.kryption.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.io.Serializable
import java.time.ZonedDateTime
import java.util.Date
import javax.validation.constraints.NotBlank

@Document(collection = "chats")
data class ChatMessage(
    @NotBlank var message: String? = null,
    var fromId: String? = null,
    var toId: String? = null,
    var timestamp: Date? = null,
    var imageHeight: Int? = null,
    var imageWidth: Int? = null,
    var imageUrl: String? = null
) : Serializable {
    @Id
    var id: String? = null
}
