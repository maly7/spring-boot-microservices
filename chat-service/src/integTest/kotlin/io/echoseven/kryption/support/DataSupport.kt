package io.echoseven.kryption.support

import com.fasterxml.jackson.databind.ObjectMapper
import io.echoseven.kryption.web.resource.ContactRequest
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import java.util.UUID
import kotlin.collections.set

val objectMapper: ObjectMapper = ObjectMapper()
const val AUTH_SERVICE_PORT: Int = 8089

fun authHeaders(token: String): HttpHeaders {
    val headers = HttpHeaders()
    headers.accept = listOf(MediaType.APPLICATION_JSON_UTF8)
    headers[HttpHeaders.AUTHORIZATION] = listOf(token)
    return headers
}

fun generateContactRequest() = ContactRequest("${UUID.randomUUID()}@email.com")
