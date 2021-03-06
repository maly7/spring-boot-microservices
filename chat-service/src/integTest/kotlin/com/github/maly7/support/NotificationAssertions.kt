package com.github.maly7.support

import com.beust.klaxon.Klaxon
import com.github.maly7.domain.Conversation
import com.github.maly7.domain.ConversationMessage
import com.github.maly7.notify.Notification
import com.github.maly7.notify.NotificationStatus
import org.hamcrest.CoreMatchers
import org.hamcrest.MatcherAssert
import org.springframework.amqp.core.Message
import java.io.StringReader
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

val klaxon = Klaxon()

fun assertNewConversationNotification(message: Message, conversation: Conversation, fromId: String, toId: String) {
    assertNotNull(message)

    val messageBody = String(message.body)
    val notification = klaxon.parse<Notification>(messageBody)!!
    val content = klaxon.parseJsonObject(StringReader(messageBody))["content"] as Map<*, *>
    val participants = content["participants"] as List<*>
    assertEquals(
        NotificationStatus.NEW_CONVERSATION,
        notification.status,
        "The notification should be for a new conversation"
    )
    assertEquals(conversation.id, notification.conversationId, "The notification should be for this conversation")
    assertEquals(conversation.id, content["id"], "The notification content is should be the conversation id")
    assertTrue(
        notification.messageId.isNullOrEmpty(),
        "There should be no message associated with this notification"
    )
    MatcherAssert.assertThat(
        "The sender is a participant of the conversation from the notification",
        participants,
        CoreMatchers.hasItem(fromId)
    )
    MatcherAssert.assertThat(
        "The recipient is a participant in the conversation from the notification",
        participants,
        CoreMatchers.hasItem(toId)
    )
}

fun assertNewMessageNotification(
    message: Message,
    conversation: Conversation,
    conversationMessage: ConversationMessage
) {
    assertNotNull(message, "A new message notification should be sent")

    val messageBody = String(message.body)
    val notification = klaxon.parse<Notification>(messageBody)!!
    val content = klaxon.parseJsonObject(StringReader(messageBody))["content"] as Map<*, *>

    assertEquals(
        NotificationStatus.NEW_MESSAGE,
        notification.status,
        "The notification should be for a new message"
    )
    assertEquals(
        conversation.id,
        notification.conversationId,
        "The notification should be for conversation ${conversation.id}"
    )
    assertEquals(
        conversationMessage.id,
        notification.messageId,
        "The notification should be for the message $conversationMessage"
    )
    assertEquals(conversationMessage.message, content["message"], "The message text should be in the notification")
}

fun assertDeleteConversationNotification(message: Message, conversation: Conversation) {
    assertNotNull(message, "A delete conversation notification should be sent")

    val messageBody = String(message.body)
    val notification = klaxon.parse<Notification>(messageBody)!!
    val content = klaxon.parseJsonObject(StringReader(messageBody))["content"] as String

    assertEquals(
        NotificationStatus.DELETE_CONVERSATION,
        notification.status,
        "The notification should be conversation deletion"
    )
    assertEquals(
        conversation.id,
        notification.conversationId,
        "The notification should be for conversation ${conversation.id}"
    )
    assertTrue(notification.messageId.isNullOrEmpty(), "There should be no associated message id")
    assertTrue(content.isEmpty(), "There should be no content")
}

fun assertDeleteMessageNotification(
    message: Message,
    conversation: Conversation,
    conversationMessage: ConversationMessage
) {
    assertNotNull(message, "A delete message notification should be sent")

    val messageBody = String(message.body)
    val notification = klaxon.parse<Notification>(messageBody)!!
    val content = klaxon.parseJsonObject(StringReader(messageBody))["content"] as String

    assertEquals(
        NotificationStatus.DELETE_MESSAGE,
        notification.status,
        "The notification should be for a deleted message"
    )
    assertEquals(
        conversation.id,
        notification.conversationId,
        "The notification should be for conversation ${conversation.id}"
    )
    assertEquals(
        conversationMessage.id,
        notification.messageId,
        "The notification should be for the message $conversationMessage"
    )
    assertTrue(content.isEmpty(), "There should be no content")
}
