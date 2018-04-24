package io.echoseven.kryption.support

import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity

fun <T> TestRestTemplate.getForEntity(url: String, headers: HttpHeaders, responseType: Class<T>): ResponseEntity<T> {
    val entity = HttpEntity("", headers)
    return this.exchange(url, HttpMethod.GET, entity, responseType)
}
