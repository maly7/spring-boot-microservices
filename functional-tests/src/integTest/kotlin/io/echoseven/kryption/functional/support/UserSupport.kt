package io.echoseven.kryption.functional.support

import io.restassured.RestAssured.given
import io.restassured.http.Header
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

fun userAuthJson(email: String, password: String): String {
    val user = mapOf("email" to email, "password" to password)
    return toJson(user)
}

fun contactJson(email: String): String {
    val contact = mapOf("email" to email)
    return toJson(contact)
}

fun createUser(email: String, password: String) =
    given()
        .body(userAuthJson(email, password))
        .header(Header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE))
    .When()
        .post("/user/registration")

fun login(email: String, password: String) =
    given()
        .body(userAuthJson(email, password))
        .header(Header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE))
    .When()
        .post("/login")
    .then()
        .extract().path<String>("token")

fun loginNewUser(email: String = email(), password: String = password()): String {
    createUser(email, password)
    return login(email, password)
}

fun createContactForUser(
    authToken: String,
    contactEmail: String = email(),
    contactPassword: String = password()
): String {
    createUser(contactEmail, contactPassword)

    return given()
        .body(contactJson(contactEmail))
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE)
        .header(HttpHeaders.AUTHORIZATION, authToken)
    .When()
        .post("/chat/contacts")
    .then()
        .extract().path<String>("[0].id")
}
