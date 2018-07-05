package io.echoseven.kryption.functional

import io.echoseven.kryption.functional.support.FunctionalTest
import io.echoseven.kryption.functional.support.When
import io.echoseven.kryption.functional.support.createUser
import io.echoseven.kryption.functional.support.email
import io.echoseven.kryption.functional.support.password
import io.echoseven.kryption.functional.support.userAuthJson
import io.restassured.RestAssured.given
import io.restassured.http.Header
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matchers.isEmptyOrNullString
import org.junit.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

class UserRegistrationTests : FunctionalTest() {

    @Test
    fun `A new User should be able to register`() {
        val email = email()
        given()
            .body(userAuthJson(email, password()))
            .header(Header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE))
        .When()
            .post("/user/registration")
        .then()
            .body("email", equalTo(email))
            .body("id", not(isEmptyOrNullString()))
            .statusCode(HttpStatus.OK.value())
    }

    @Test
    fun `A User should not be able to register twice`() {
        val email = email()
        createUser(email, password())

        createUser(email, password()).then()
            .statusCode(HttpStatus.BAD_REQUEST.value())
    }
}

