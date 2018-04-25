package io.echoseven.kryption.functional

import io.echoseven.kryption.functional.support.When
import io.echoseven.kryption.functional.support.contactJson
import io.echoseven.kryption.functional.support.createUser
import io.echoseven.kryption.functional.support.email
import io.echoseven.kryption.functional.support.login
import io.echoseven.kryption.functional.support.password
import io.restassured.RestAssured.given
import org.hamcrest.CoreMatchers.equalTo
import org.junit.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

class ContactTests {

    @Test
    fun `A User should be able to add another user as a contact`() {
        val contactEmail = email()
        createUser(contactEmail, password())
            .then().statusCode(HttpStatus.OK.value())

        val userEmail = email()
        val userPassword = password()

        createUser(userEmail, userPassword)
            .then().statusCode(HttpStatus.OK.value())

        val token = login(userEmail, userPassword)

        given()
            .body(contactJson(contactEmail))
            .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
            .header(HttpHeaders.AUTHORIZATION, token)
        .When()
            .post("/chat/contacts")
        .then()
            .body("[0].email", equalTo(contactEmail))

        given()
            .header(HttpHeaders.AUTHORIZATION, token)
        .When()
            .get("/chat/contacts")
        .then()
            .body("[0].email", equalTo(contactEmail))
    }
}
