package com.github.maly7.notify

data class Notification(
    val status: NotificationStatus,
    val conversationId: String?,
    val messageId: String?,
    val content: Any?
)
