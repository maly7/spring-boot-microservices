package io.echoseven.kryption.functional

import io.echoseven.kryption.functional.support.When
import io.echoseven.kryption.functional.support.createContactForUser
import io.echoseven.kryption.functional.support.loginNewUser
import io.echoseven.kryption.functional.support.messageJson
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

class ConversationTests {

    @Test
    fun `A User should be able to send a contact a message`() {
        val userToken = loginNewUser()
        val contactId = createContactForUser(userToken)
        val message = "First message sent"

        given()
            .body(messageJson(contactId, message))
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
            .header(HttpHeaders.AUTHORIZATION, userToken)
        .When()
            .post("/chat/conversation/message")
        .then()
            .body("messages[0].message", equalTo(message))
    }

    @Test
    fun `A User should be able to delete a conversation`() {
    }

    @Test
    fun `A User should be able to delete a message`() {
    }
}
