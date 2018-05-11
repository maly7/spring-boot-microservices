package io.echoseven.kryption.web.resource

data class Contact(
    val id: String?,
    val email: String,
    val onlineStatus: Boolean,
    val profileImageUrl: String
)
