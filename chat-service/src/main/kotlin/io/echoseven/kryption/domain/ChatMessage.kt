package io.echoseven.kryption.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.time.ZonedDateTime
import javax.validation.constraints.NotBlank

@Document(collection = "chats")
data class ChatMessage(
    @NotBlank var message: String,
    var fromId: String,
    var toId: String,
    var timestamp: ZonedDateTime,
    var imageHeight: Int? = null,
    var imageWidth: Int? = null,
    var imageUrl: String? = null
) {
    @Id
    var id: String? = null
}
