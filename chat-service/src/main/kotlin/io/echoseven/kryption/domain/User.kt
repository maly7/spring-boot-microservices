package io.echoseven.kryption.domain

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.index.Indexed
import org.springframework.data.mongodb.core.mapping.Document
import javax.validation.constraints.NotBlank

@Document(collection = "users")
data class User(@NotBlank @Indexed(name = "email", unique = true) var email: String = "placeholder-email",
                var name: String = "Tap to update your name",
                var onlineStatus: Boolean = false,
                var profileImageUrl: String = "noPhoto") {
    @Id
    var id: String? = null
}
