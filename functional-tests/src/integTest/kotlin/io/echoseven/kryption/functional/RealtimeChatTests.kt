package io.echoseven.kryption.functional

import io.echoseven.kryption.functional.support.WEBSOCKET_URI
import io.echoseven.kryption.functional.support.email
import io.echoseven.kryption.functional.support.getUserId
import io.echoseven.kryption.functional.support.loginNewUser
import io.echoseven.kryption.functional.support.password
import io.echoseven.kryption.functional.support.queueName
import org.junit.Before
import org.junit.Test
import org.springframework.http.HttpHeaders
import org.springframework.messaging.converter.MappingJackson2MessageConverter
import org.springframework.messaging.simp.stomp.StompHeaders
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter
import org.springframework.web.socket.WebSocketHttpHeaders
import org.springframework.web.socket.client.standard.StandardWebSocketClient
import org.springframework.web.socket.messaging.WebSocketStompClient
import java.util.concurrent.LinkedBlockingDeque
import kotlin.test.assertTrue

class RealtimeChatTests {
    // check out: http://rafaelhz.github.io/testing-websockets/
    lateinit var stompClient: WebSocketStompClient
    lateinit var userToken: String
    lateinit var userId: String
    val blockingQueue = LinkedBlockingDeque<String>()
    val username = email()

    @Before
    fun setup() {
        userToken = loginNewUser(username, password())
        userId = getUserId(userToken)

        stompClient = WebSocketStompClient(StandardWebSocketClient())
        stompClient.messageConverter = MappingJackson2MessageConverter()
    }

    @Test
    fun `A User should be able to subscribe to their queue`() {
        val stompHeaders = StompHeaders()
        stompHeaders[StompHeaders.LOGIN] = username
        stompHeaders[StompHeaders.PASSCODE] = userToken
        stompHeaders[StompHeaders.DESTINATION] = queueName(userId)
        val adapter = object : StompSessionHandlerAdapter() {}
        val webSocketHttpHeaders = WebSocketHttpHeaders()
        webSocketHttpHeaders[HttpHeaders.UPGRADE] = "websocket"
        val session = stompClient.connect(WEBSOCKET_URI, webSocketHttpHeaders, stompHeaders, adapter).get()

        session.subscribe(stompHeaders, adapter)

        assertTrue(session.isConnected, "The user session should be connected")
    }

    @Test
    fun `A user cannot subscribe to another user queue`() {
    }

    @Test
    fun `A User should receive updates to conversations via their queue`() {
    }
}
