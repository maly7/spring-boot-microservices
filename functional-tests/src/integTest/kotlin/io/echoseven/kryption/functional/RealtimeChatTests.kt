package io.echoseven.kryption.functional

import io.echoseven.kryption.functional.support.TestingStompSessionHandlerAdapter
import io.echoseven.kryption.functional.support.buildStompHeaders
import io.echoseven.kryption.functional.support.connect
import io.echoseven.kryption.functional.support.createContactForUser
import io.echoseven.kryption.functional.support.email
import io.echoseven.kryption.functional.support.getUserId
import io.echoseven.kryption.functional.support.login
import io.echoseven.kryption.functional.support.loginNewUser
import io.echoseven.kryption.functional.support.password
import io.echoseven.kryption.functional.support.sendMessage
import org.junit.Before
import org.junit.Test
import org.springframework.messaging.converter.MappingJackson2MessageConverter
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.messaging.WebSocketStompClient
import kotlin.test.assertTrue

class RealtimeChatTests {
    private lateinit var stompClient: WebSocketStompClient
    private lateinit var userToken: String
    private lateinit var contactToken: String
    private lateinit var userId: String
    private lateinit var contactId: String
    private val username = email()
    private val contact = email()

    @Before
    fun setup() {
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

        for (i in 1..5) {
            sendMessage(userToken, contactId, "Test Message $i")
        }

        for (i in 1..5) {
            sendMessage(contactToken, userId, "Reply Message $i")
        }

        Thread.sleep(5000)

        assertTrue(adapter.messages.isNotEmpty())
    }

    @Test
    fun `A user cannot subscribe to another user queue`() {
        val stompHeaders = buildStompHeaders(contact, contactToken, userId)
        val adapter = TestingStompSessionHandlerAdapter()
        val session = stompClient.connect(stompHeaders, adapter)

        session.subscribe(stompHeaders, adapter)

        for (i in 1..5) {
            sendMessage(userToken, contactId, "Test Message $i")
        }

        for (i in 1..5) {
            sendMessage(contactToken, userId, "Reply Message $i")
        }

        Thread.sleep(5000)

        assertTrue(adapter.messages.isEmpty())
    }

    @Test
    fun `A User should receive updates to conversations via their queue`() {
    }
}
