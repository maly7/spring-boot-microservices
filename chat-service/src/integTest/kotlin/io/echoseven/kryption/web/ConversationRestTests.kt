package io.echoseven.kryption.web

import io.echoseven.kryption.ChatIntegrationTest
import io.echoseven.kryption.domain.Conversation
import io.echoseven.kryption.extensions.deleteEntity
import io.echoseven.kryption.extensions.getForEntity
import io.echoseven.kryption.extensions.sendConversationMessage
import io.echoseven.kryption.support.ConversationSupport
import io.echoseven.kryption.support.authHeaders
import org.hamcrest.Matchers.`is`
import org.hamcrest.Matchers.hasSize
import org.junit.Assert.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit4.SpringRunner
import java.util.UUID
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull

@RunWith(SpringRunner::class)
@ChatIntegrationTest
class ConversationRestTests : ConversationSupport() {

    @Test
    fun `Users should be able to send messages`() {
        val messageText = "Initial Message"
        val response = restTemplate.sendConversationMessage(userToken, contact.id!!, messageText)
        val conversation = response.body!!

        assertEquals(HttpStatus.OK, response.statusCode, "The status code should be 200 successful")
        assertThat("There should be only one message", conversation.messages, hasSize(1))
        val firstMessage = conversation.messages.first()

        assertEquals(messageText, firstMessage.message, "The message text is $messageText")
        assertEquals(currentUser.id, firstMessage.fromId, "The from id should be from the sending user")
        assertEquals(contact.id, firstMessage.toId, "The to id should be the contact")
        assertNotNull(firstMessage.timestamp, "There should be a timestamp")

        val reply = "Reply message"
        val replyResponse = restTemplate.sendConversationMessage(contactToken, currentUser.id!!, reply)
        val replyConversation = replyResponse.body!!

        assertEquals(HttpStatus.OK, replyResponse.statusCode, "A user should be able to reply")
        assertThat("There should now be two messages in the conversation", replyConversation.messages, hasSize(2))

        val replyMessage = replyConversation.messages.last()
        assertEquals(reply, replyMessage.message, "The message text is $reply")
        assertEquals(currentUser.id, replyMessage.toId, "The to id should be the first user")
        assertEquals(contact.id, replyMessage.fromId, "The from id should be the contact")
        assertNotNull(replyMessage.timestamp, "There should be a timestamp")
    }

    @Test
    fun `Users should not be able to initiate conversations to non-contacts`() {
        val failedResponse = restTemplate.sendConversationMessage(userToken, "${UUID.randomUUID()}", "Doesn't matter")
        assertEquals(HttpStatus.BAD_REQUEST, failedResponse.statusCode, "The status code should be 400 Bad Request")
    }

    @Test
    fun `Participants should be able to retrieve conversations`() {
        val conversationId = restTemplate.sendConversationMessage(userToken, contact.id!!, "Some text").body!!.id

        val response =
            restTemplate.getForEntity("/conversation/$conversationId", authHeaders(userToken), Conversation::class.java)

        assertEquals(HttpStatus.OK, response.statusCode, "The response should be 200 OK")
        assertThat("There should be a message", response.body!!.messages, hasSize(1))

        val otherUserResponse = restTemplate.getForEntity(
            "/conversation/$conversationId",
            authHeaders(contactToken),
            Conversation::class.java
        )
        assertEquals(HttpStatus.OK, otherUserResponse.statusCode, "The response should be 200 OK for the other user")
        assertEquals(response.body!!, otherUserResponse.body!!, "The two conversations should be the same")
    }

    @Test
    fun `Non-participants should not be able to retrieve conversations`() {
        val conversationId = restTemplate.sendConversationMessage(userToken, contact.id!!, "Some text").body!!.id
        val failedResponse =
            restTemplate.getForEntity("/conversation/$conversationId", authHeaders(otherUserToken), String::class.java)

        assertEquals(HttpStatus.FORBIDDEN, failedResponse.statusCode, "The response should be 403 Forbidden")
    }

    @Test
    fun `The creator should be able to delete conversations`() {
        val conversationId = restTemplate.sendConversationMessage(userToken, contact.id!!, "Some text").body!!.id!!
        val response = restTemplate.deleteEntity("/conversation/$conversationId", authHeaders(userToken))

        assertEquals(HttpStatus.OK, response.statusCode, "The response should be 200 OK")
        assertFalse(conversationRepository.findById(conversationId).isPresent, "The conversation should be deleted")

        assertThat(
            "There should be no messages from the current user",
            conversationMessageRepository.findAllByFromId(currentUser.id!!),
            `is`(emptyList())
        )

        assertThat(
            "There should be no messages to the contact",
            conversationMessageRepository.findAllByToId(contact.id!!),
            `is`(
                emptyList()
            )
        )
    }

    @Test
    fun `The other participant should be able to delete conversations`() {
        val conversationId = restTemplate.sendConversationMessage(userToken, contact.id!!, "Some text").body!!.id!!
        val response = restTemplate.deleteEntity("/conversation/$conversationId", authHeaders(contactToken))

        assertEquals(HttpStatus.OK, response.statusCode, "The response should be 200 OK")
        assertFalse(conversationRepository.findById(conversationId).isPresent, "The conversation should be deleted")

        assertThat(
            "There should be no messages from the current user",
            conversationMessageRepository.findAllByFromId(currentUser.id!!),
            `is`(emptyList())
        )

        assertThat(
            "There should be no messages to the contact",
            conversationMessageRepository.findAllByToId(contact.id!!),
            `is`(
                emptyList()
            )
        )
    }

    @Test
    fun `Non-Participants should not be able to delete other conversations`() {
        val conversationId = restTemplate.sendConversationMessage(userToken, contact.id!!, "Some text").body!!.id
        val response = restTemplate.deleteEntity("/conversation/$conversationId", authHeaders(otherUserToken))

        assertEquals(HttpStatus.FORBIDDEN, response.statusCode, "The response should be 403 Forbidden")
    }

    @Test
    fun `Participants should be able to delete messages`() {
        restTemplate.sendConversationMessage(userToken, contact.id!!, "Some text").body!!
        val conversation = restTemplate.sendConversationMessage(userToken, contact.id!!, "Another message").body!!
        val messageId = conversation.messages.first().id!!
        val otherMessageId = conversation.messages.last().id!!

        val response = restTemplate.deleteEntity("/conversation/message/$messageId", authHeaders(userToken))
        assertEquals(HttpStatus.OK, response.statusCode, "The sender should be able to delete")
        assertFalse(conversationMessageRepository.findById(messageId).isPresent, "The message should be deleted")

        val otherDeleteResponse =
            restTemplate.deleteEntity("/conversation/message/$otherMessageId", authHeaders(contactToken))
        assertEquals(HttpStatus.OK, otherDeleteResponse.statusCode, "The receiver should be able to delete")
        assertFalse(
            conversationMessageRepository.findById(otherMessageId).isPresent,
            "The other message should be deleted"
        )
        assertFalse(
            conversationRepository.findById(conversation.id!!).isPresent,
            "With no more messages the conversation should be deleted"
        )
    }

    @Test
    fun `Non-Participants should not be able to delete other messages`() {
        val messageId =
            restTemplate.sendConversationMessage(userToken, contact.id!!, "New message").body!!.messages.first().id!!

        val failedResponse = restTemplate.deleteEntity("/conversation/message/$messageId", authHeaders(otherUserToken))
        assertEquals(HttpStatus.FORBIDDEN, failedResponse.statusCode, "The delete should fail with 403 forbidden")
    }
}
