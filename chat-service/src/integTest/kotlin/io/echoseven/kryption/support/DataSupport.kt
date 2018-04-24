package io.echoseven.kryption.support

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit.WireMockRule
import io.echoseven.kryption.clients.AuthUser
import io.echoseven.kryption.domain.User
import io.echoseven.kryption.web.resource.ContactRequest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import java.util.UUID

val objectMapper: ObjectMapper = ObjectMapper()
const val AUTH_SERVICE_PORT: Int = 8089

fun stubAuthUser(rule: WireMockRule, token: String, user: AuthUser) {
    rule.stubFor(get(urlEqualTo("/authenticate"))
        .withHeader(HttpHeaders.AUTHORIZATION, containing(token))
        .willReturn(aResponse()
            .withStatus(HttpStatus.OK.value())
            .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .withBody(objectMapper.writeValueAsString(user))))
}

fun stubAuthUser(rule: WireMockRule, token: String, user: User) =
    stubAuthUser(rule, token, AuthUser(user.id.toString(), user.email, true))

fun authHeaders(token: String): HttpHeaders {
    val headers = HttpHeaders()
    headers.accept = listOf(MediaType.APPLICATION_JSON_UTF8)
    headers[HttpHeaders.AUTHORIZATION] = listOf(token)
    return headers
}

fun createUser(user: User, restTemplate: TestRestTemplate): User {
    return restTemplate.postForEntity("/user", user, User::class.java).body!!
}

fun createUserAsContact(restTemplate: TestRestTemplate, token: String, contact: ContactRequest = contactRequest()): String {
    createUser(User(contact.email!!), restTemplate)

    val contactRequestEntity = HttpEntity(ContactRequest(contact.email), authHeaders(token))
    restTemplate.postForEntity("/contacts", contactRequestEntity, String::class.java)
    return contact.email.toString()
}

fun addContact(restTemplate: TestRestTemplate, token: String, contact: User): ResponseEntity<String>? {
    val contactRequestEntity = HttpEntity(ContactRequest(contact.email), authHeaders(token))
    return restTemplate.postForEntity("/contacts", contactRequestEntity, String::class.java)
}

fun contactRequest(email: String = "${UUID.randomUUID()}@email.com") = ContactRequest(email)
