package io.echoseven.kryption.notify

data class Notification(
    val status: NotificationStatus,
    val conversationId: String?,
    val messageId: String?,
    val content: Any?
)
