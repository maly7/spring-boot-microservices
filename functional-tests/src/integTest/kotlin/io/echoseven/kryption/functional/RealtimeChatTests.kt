package io.echoseven.kryption.functional

import org.junit.Test
import org.springframework.web.socket.messaging.WebSocketStompClient

class RealtimeChatTests {
    // check out: http://rafaelhz.github.io/testing-websockets/
    lateinit var websocketClient: WebSocketStompClient

    @Test
    fun `A User should be able to subscribe to their queue`() {
    }

    @Test
    fun `A user cannot subscribe to another user queue`() {
    }

    @Test
    fun `A User should receive updates to conversations via their queue`() {
    }
}
