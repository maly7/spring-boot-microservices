package io.echoseven.kryption.functional.support

import com.beust.klaxon.Klaxon
import io.restassured.RestAssured.given
import io.restassured.http.Header
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType

fun toJson(map: Map<String, String>): String = Klaxon().toJsonString(map)

fun userAuthJson(email: String, password: String): String {
    val user = mapOf("email" to email, "password" to password)
    return toJson(user)
}

fun createUser(email: String, password: String) =
    given()
        .body(userAuthJson(email, password))
        .header(Header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_UTF8_VALUE))
        .When()
        .post("/user/registration")
