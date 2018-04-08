package io.echoseven.kryption.functional

import io.echoseven.kryption.functional.support.When
import io.echoseven.kryption.functional.support.createUser
import io.echoseven.kryption.functional.support.email
import io.echoseven.kryption.functional.support.password
import io.echoseven.kryption.functional.support.userAuthJson
import io.restassured.RestAssured.given
import io.restassured.http.Header
import org.hamcrest.Matchers.*
import org.junit.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

class UserLoginTests {

    @Test
    fun `A User should be able to login with their credentials`() {
        val email = email()
        val password = password()

        createUser(email, password)

        given()
            .body(userAuthJson(email, password))
            .header(Header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE))
        .When()
            .post("/login")
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("email", equalTo(email))
            .body("token", not(isEmptyOrNullString()))
            .body("onlineStatus", equalTo(false))
            .body("profileImageUrl", equalTo("noPhoto"))
            .body("name", equalTo("Tap to update your name"))
    }
}
