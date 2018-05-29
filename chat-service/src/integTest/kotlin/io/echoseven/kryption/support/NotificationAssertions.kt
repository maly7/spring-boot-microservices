package io.echoseven.kryption.support

import com.beust.klaxon.Klaxon
import io.echoseven.kryption.domain.Conversation
import io.echoseven.kryption.domain.ConversationMessage
import io.echoseven.kryption.notify.Notification
import io.echoseven.kryption.notify.NotificationStatus
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
