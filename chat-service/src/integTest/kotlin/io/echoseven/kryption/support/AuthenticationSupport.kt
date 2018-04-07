package io.echoseven.kryption.support

import com.fasterxml.jackson.databind.ObjectMapper
import com.github.tomakehurst.wiremock.client.WireMock.*
import com.github.tomakehurst.wiremock.junit.WireMockRule
import io.echoseven.kryption.clients.AuthUser
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

val objectMapper: ObjectMapper = ObjectMapper()

fun stubAuthUser(rule: WireMockRule, token: String, user: AuthUser) {
    rule.stubFor(get(urlEqualTo("/authenticate"))
        .withHeader(HttpHeaders.AUTHORIZATION, containing(token))
        .willReturn(aResponse()
            .withStatus(HttpStatus.OK.value())
            .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .withBody(objectMapper.writeValueAsString(user))))
}