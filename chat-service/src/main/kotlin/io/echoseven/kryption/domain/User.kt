package io.echoseven.kryption.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "users")
data class User(var email: String = "placeholder-email",
                var name: String = "Tap to update your name",
                var onlineStatus: Boolean = false,
                var profileImageUrl: String = "noPhoto") {
    @Id
    var id: String? = null
}
