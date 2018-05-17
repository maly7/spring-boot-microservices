package io.echoseven.kryption.extensions

import io.echoseven.kryption.domain.Chat
import io.echoseven.kryption.domain.ChatMessage
import io.echoseven.kryption.domain.User
import io.echoseven.kryption.support.authHeaders
import io.echoseven.kryption.support.generateContactRequest
import io.echoseven.kryption.web.resource.ContactRequest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity

fun <T> TestRestTemplate.getForEntity(uri: String, headers: HttpHeaders, responseType: Class<T>): ResponseEntity<T> {
    val entity = HttpEntity("", headers)
    return this.exchange(uri, HttpMethod.GET, entity, responseType)
}

fun TestRestTemplate.deleteEntity(uri: String, headers: HttpHeaders): ResponseEntity<Void> {
    val entity = HttpEntity("", headers)
    return this.exchange(uri, org.springframework.http.HttpMethod.DELETE, entity, Void::class.java)
}

fun TestRestTemplate.createUser(user: User) = this.postForEntity("/user", user, User::class.java).body!!

fun TestRestTemplate.addContact(authToken: String, contact: User): ResponseEntity<String> {
    val requestEntity = HttpEntity(ContactRequest(contact.email), authHeaders(authToken))
    return this.postForEntity("/contacts", requestEntity, String::class.java)
}

fun TestRestTemplate.createUserAsContact(
    authToken: String,
    contact: ContactRequest = generateContactRequest()
): String {
    this.createUser(User(contact.email!!))

    val requestEntity = HttpEntity(contact, authHeaders(authToken))
    this.postForEntity("/contacts", requestEntity, String::class.java)
    return contact.email!!
}

fun TestRestTemplate.getContacts(authToken: String) =
    this.getForEntity("/contacts", authHeaders(authToken), List::class.java)

fun TestRestTemplate.deleteContact(authToken: String, email: String) {
    val requestEntity = HttpEntity(ContactRequest(email), authHeaders(authToken))
    this.exchange("/contacts", HttpMethod.DELETE, requestEntity, Any::class.java)
}

fun TestRestTemplate.sendChatMessage(authToken: String, toId: String, message: String): ResponseEntity<Chat> {
    val chatMessage = ChatMessage(message = message, toId = toId)
    val requestEntity = HttpEntity(chatMessage, authHeaders(authToken))
    return this.postForEntity("/conversation/message", requestEntity, Chat::class.java)
}
