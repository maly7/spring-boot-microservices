package io.echoseven.kryption.functional

import io.echoseven.kryption.functional.support.FunctionalTest
import io.echoseven.kryption.functional.support.When
import io.echoseven.kryption.functional.support.createContactForUser
import io.echoseven.kryption.functional.support.givenAuthHeader
import io.echoseven.kryption.functional.support.loginNewUser
import io.echoseven.kryption.functional.support.sendMessage
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Before
import org.junit.Test
import org.springframework.http.HttpStatus

class ConversationTests : FunctionalTest() {

    lateinit var userToken: String
    lateinit var contactId: String

    @Before
    override fun setup() {
        super.setup()
        userToken = loginNewUser()
        contactId = createContactForUser(userToken)
    }

    @Test
    fun `A User should be able to send a contact a message`() {
        val message = "First message sent"

        sendMessage(userToken, contactId, message)
            .then().body("messages[0].message", equalTo(message))
    }

    @Test
    fun `A User should be able to delete a conversation`() {
        val chatId = sendMessage(userToken, contactId, "Another message")
            .then().extract().path<String>("id")

        givenAuthHeader(userToken)
        .When()
            .delete("/chat/conversation/$chatId")
        .then()
            .statusCode(HttpStatus.NO_CONTENT.value())

        givenAuthHeader(userToken)
        .When()
            .get("/chat/conversation/$chatId")
        .then()
            .statusCode(HttpStatus.FORBIDDEN.value())
    }

    @Test
    fun `A User should be able to delete a message`() {
        val messageId = sendMessage(userToken, contactId, "Delete Me Pls")
            .then().extract().path<String>("messages[0].id")

        givenAuthHeader(userToken)
        .When()
            .delete("/chat/conversation/message/$messageId")
        .then()
            .statusCode(HttpStatus.NO_CONTENT.value())
    }
}
