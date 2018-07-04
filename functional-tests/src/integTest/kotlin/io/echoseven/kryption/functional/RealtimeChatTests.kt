package io.echoseven.kryption.functional

import io.echoseven.kryption.functional.support.FunctionalTest
import io.echoseven.kryption.functional.support.TestingStompSessionHandlerAdapter
import io.echoseven.kryption.functional.support.buildStompHeaders
import io.echoseven.kryption.functional.support.connect
import io.echoseven.kryption.functional.support.createContactForUser
import io.echoseven.kryption.functional.support.deleteConversationThenWait
import io.echoseven.kryption.functional.support.deleteMessageThenWait
import io.echoseven.kryption.functional.support.email
import io.echoseven.kryption.functional.support.extensions.containsDeleteConversation
import io.echoseven.kryption.functional.support.extensions.containsDeleteMessage
import io.echoseven.kryption.functional.support.extensions.containsNewConversation
import io.echoseven.kryption.functional.support.extensions.containsNewMessage
import io.echoseven.kryption.functional.support.extensions.findNewConversation
import io.echoseven.kryption.functional.support.extensions.findNewMessage
import io.echoseven.kryption.functional.support.getUserId
import io.echoseven.kryption.functional.support.login
import io.echoseven.kryption.functional.support.loginNewUser
import io.echoseven.kryption.functional.support.password
import io.echoseven.kryption.functional.support.sendAndReplyMessages
import org.junit.Before
import org.junit.Test
import org.springframework.messaging.converter.MappingJackson2MessageConverter
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.messaging.WebSocketStompClient
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class RealtimeChatTests : FunctionalTest() {
    private lateinit var stompClient: WebSocketStompClient
    private lateinit var userToken: String
    private lateinit var contactToken: String
    private lateinit var userId: String
    private lateinit var contactId: String
    private val username = email()
    private val contact = email()

    @Before
    override fun setup() {
        super.setup()

        userToken = loginNewUser(username, password())
        userId = getUserId(userToken)

        val contactPassword = password()
        contactId = createContactForUser(userToken, contact, contactPassword)
        contactToken = login(contact, contactPassword)

        stompClient = WebSocketStompClient(StandardWebSocketClient())
        stompClient.messageConverter = MappingJackson2MessageConverter()
    }

    @Test
    fun `A User should be able to subscribe to their queue`() {
        val stompHeaders = buildStompHeaders(username, userToken, userId)
        val adapter = TestingStompSessionHandlerAdapter()
        val session = stompClient.connect(stompHeaders, adapter)

        session.subscribe(stompHeaders, adapter)
        assertTrue(session.isConnected, "The user session should be connected")

        sendAndReplyMessages(userToken, contactToken, userId, contactId)
        assertTrue(adapter.messages.isNotEmpty(), "The user has received messages")
    }

    @Test
    fun `A user cannot subscribe to another user queue`() {
        val stompHeaders = buildStompHeaders(contact, contactToken, userId)
        val adapter = TestingStompSessionHandlerAdapter()
        val session = stompClient.connect(stompHeaders, adapter)

        session.subscribe(stompHeaders, adapter)
        sendAndReplyMessages(userToken, contactToken, userId, contactId)

        assertTrue(adapter.messages.isEmpty(), "The user is unable to receive messages")
    }

    @Test
    @Suppress("UNCHECKED_CAST")
    fun `Users should receive updates to conversations via their queue`() {
        val userHeaders = buildStompHeaders(username, userToken, userId)
        val userAdapter = TestingStompSessionHandlerAdapter()
        val userSession = stompClient.connect(userHeaders, userAdapter)
        userSession.subscribe(userHeaders, userAdapter)

        val contactHeaders = buildStompHeaders(contact, contactToken, contactId)
        val contactAdapter = TestingStompSessionHandlerAdapter()
        val contactSession = stompClient.connect(contactHeaders, contactAdapter)
        contactSession.subscribe(contactHeaders, contactAdapter)

        sendAndReplyMessages(userToken, contactToken, userId, contactId)

        var userMessages = userAdapter.messages as List<LinkedHashMap<*, *>>
        var contactMessages = contactAdapter.messages as List<LinkedHashMap<*, *>>

        assertTrue(userMessages.isNotEmpty(), "The user should receive messages")
        assertTrue(contactMessages.isNotEmpty(), "The contact should receive messages")

        assertTrue(userMessages.containsNewMessage(), "The user should receive new message notifications")
        assertTrue(contactMessages.containsNewMessage(), "The contact should receive new messages notifications")

        assertFalse(
            userMessages.containsNewConversation(),
            "The user initiating the conversation should not have a new conversation message"
        )
        assertTrue(
            contactMessages.containsNewConversation(),
            "The contact receiving the new conversation should have a new conversation notification"
        )

        val messageId = userMessages.findNewMessage()!!["messageId"].toString()
        val conversationId = contactMessages.findNewConversation()!!["conversationId"].toString()

        deleteMessageThenWait(userToken, messageId)
        deleteConversationThenWait(contactToken, conversationId)

        userMessages = userAdapter.messages as List<LinkedHashMap<*, *>>
        contactMessages = contactAdapter.messages as List<LinkedHashMap<*, *>>

        assertTrue(userMessages.containsDeleteMessage(), "Both users should receive a delete message notification")
        assertTrue(contactMessages.containsDeleteMessage(), "Both users should receive a delete message notification")

        assertTrue(
            userMessages.containsDeleteConversation(),
            "Both users should receive a delete conversation notification"
        )
        assertTrue(
            contactMessages.containsDeleteConversation(),
            "Both users should receive a delete conversation notification"
        )
    }
}
