package io.echoseven.kryption.functional.support

import io.restassured.RestAssured.given
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

fun messageJson(toId: String, message: String): String {
    val conversationMessage = mapOf("toId" to toId, "message" to message)
    return toJson(conversationMessage)
}

fun sendMessage(userToken: String, toId: String, message: String) =
    given()
        .body(messageJson(toId, message))
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
        .header(HttpHeaders.AUTHORIZATION, userToken)
    .When()
        .post("/chat/conversation/message")

fun deleteConversation(userToken: String, conversationId: String) =
    givenAuthHeader(userToken)
    .When()
        .delete("/chat/conversation/$conversationId")

fun deleteMessage(userToken: String, messageId: String) =
    givenAuthHeader(userToken)
    .When()
        .delete("/chat/conversation/message/$messageId")
