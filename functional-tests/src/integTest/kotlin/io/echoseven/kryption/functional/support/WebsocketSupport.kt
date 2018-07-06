package io.echoseven.kryption.functional.support

import org.springframework.http.HttpHeaders
import org.springframework.messaging.simp.stomp.StompHeaders
import org.springframework.messaging.simp.stomp.StompSession
import org.springframework.messaging.simp.stomp.StompSessionHandler
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter
import org.springframework.web.socket.WebSocketHttpHeaders
import org.springframework.web.socket.messaging.WebSocketStompClient
import java.net.URI

const val WAIT_TIME_MILLIS = 500L

val defaultAdapter = object : StompSessionHandlerAdapter() {}

fun queueName(userId: String) = "/queue/user.$userId"

fun buildStompHeaders(username: String, authToken: String, userId: String): StompHeaders {
    val stompHeaders = StompHeaders()
    stompHeaders[StompHeaders.LOGIN] = username
    stompHeaders[StompHeaders.PASSCODE] = authToken
    stompHeaders[StompHeaders.DESTINATION] = queueName(userId)
    stompHeaders[StompHeaders.HOST] = "/"

    return stompHeaders
}

fun WebSocketStompClient.connect(
    stompHeaders: StompHeaders,
    adapter: StompSessionHandler = defaultAdapter
): StompSession {
    val webSocketHttpHeaders = WebSocketHttpHeaders()
    webSocketHttpHeaders[HttpHeaders.UPGRADE] = "websocket"

    return this.connect(getSocketUri(), webSocketHttpHeaders, stompHeaders, adapter).get()
}

fun sendAndReplyMessages(userToken: String, contactToken: String, userId: String, contactId: String) {
    for (i in 1..5) {
        sendMessage(userToken, contactId, "Test Message $i")
    }

    for (i in 1..5) {
        sendMessage(contactToken, userId, "Reply Message $i")
    }

    Thread.sleep(WAIT_TIME_MILLIS)
}

fun deleteConversationThenWait(userToken: String, conversationId: String) {
    deleteConversation(userToken, conversationId)
    Thread.sleep(WAIT_TIME_MILLIS)
}

fun deleteMessageThenWait(userToken: String, messageId: String) {
    deleteMessage(userToken, messageId)
    Thread.sleep(WAIT_TIME_MILLIS)
}

fun getSocketUri(): String {
    val uri = System.getProperty("funcTestUri", "")

    val host = if (uri.isEmpty()) {
        "localhost:8080"
    } else {
        URI(uri).host
    }

    return "ws://$host/realtime/chat"
}
