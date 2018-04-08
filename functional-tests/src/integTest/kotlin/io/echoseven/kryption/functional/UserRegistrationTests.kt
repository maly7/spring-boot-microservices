package io.echoseven.kryption.functional

import io.echoseven.kryption.functional.support.When
import io.echoseven.kryption.functional.support.userRegistrationJson
import io.restassured.RestAssured.given
import io.restassured.http.Header
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.CoreMatchers.not
import org.hamcrest.Matchers.isEmptyOrNullString
import org.junit.Test
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import java.util.UUID

class UserRegistrationTests {

    @Test
    fun `A new User should be able to register`() {
        val email = "${UUID.randomUUID()}@email.com"
        given()
            .body(userRegistrationJson(email, "${UUID.randomUUID()}"))
            .header(Header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE))
        .When()
            .post("/user/registration")
        .then()
            .body("email", equalTo(email))
            .body("id", (not(isEmptyOrNullString())))
    }
}

