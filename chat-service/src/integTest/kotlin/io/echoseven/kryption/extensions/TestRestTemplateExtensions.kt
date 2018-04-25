package io.echoseven.kryption.extensions

import io.echoseven.kryption.domain.User
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity

fun <T> TestRestTemplate.getForEntity(url: String, headers: HttpHeaders, responseType: Class<T>): ResponseEntity<T> {
    val entity = HttpEntity("", headers)
    return this.exchange(url, HttpMethod.GET, entity, responseType)
}

fun TestRestTemplate.createUser(user: User) = this.postForEntity("/user", user, User::class.java).body!!
