package io.echoseven.kryption.extensions

import com.github.tomakehurst.wiremock.client.WireMock
import com.github.tomakehurst.wiremock.junit.WireMockRule
import io.echoseven.kryption.clients.AuthUser
import io.echoseven.kryption.domain.User
import io.echoseven.kryption.support.objectMapper
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType

fun WireMockRule.stubAuthUser(authToken: String, user: AuthUser) {
    stubFor(WireMock.get(WireMock.urlEqualTo("/authenticate"))
        .withHeader(HttpHeaders.AUTHORIZATION, WireMock.containing(authToken))
        .willReturn(WireMock.aResponse()
            .withStatus(HttpStatus.OK.value())
            .withHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
            .withBody(objectMapper.writeValueAsString(user))))
}

fun WireMockRule.stubAuthUser(authToken: String, user: User) =
    this.stubAuthUser(authToken, AuthUser(user.id.toString(), user.email, true))
