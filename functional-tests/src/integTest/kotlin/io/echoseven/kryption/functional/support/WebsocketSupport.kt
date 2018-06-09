package io.echoseven.kryption.functional.support

import org.springframework.http.HttpHeaders
import org.springframework.messaging.simp.stomp.StompHeaders
import org.springframework.messaging.simp.stomp.StompSession
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter
import org.springframework.web.socket.WebSocketHttpHeaders
import org.springframework.web.socket.messaging.WebSocketStompClient

const val WEBSOCKET_URI = "ws://localhost:8080/realtime/chat"

val adapter = object : StompSessionHandlerAdapter() {}

fun queueName(userId: String) = "/queue/user.$userId"

fun buildStompHeaders(username: String, authToken: String, userId: String): StompHeaders {
    val stompHeaders = StompHeaders()
    stompHeaders[StompHeaders.LOGIN] = username
    stompHeaders[StompHeaders.PASSCODE] = authToken
    stompHeaders[StompHeaders.DESTINATION] = queueName(userId)
    return stompHeaders
}

fun WebSocketStompClient.subscribeToUserQueue(stompHeaders: StompHeaders): StompSession {
    val webSocketHttpHeaders = WebSocketHttpHeaders()
    webSocketHttpHeaders[HttpHeaders.UPGRADE] = "websocket"

    return this.connect(WEBSOCKET_URI, webSocketHttpHeaders, stompHeaders, adapter).get()
}
