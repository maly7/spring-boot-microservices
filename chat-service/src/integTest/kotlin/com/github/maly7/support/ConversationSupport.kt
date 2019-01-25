package com.github.maly7.support

import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.github.maly7.data.ConversationMessageRepository
import com.github.maly7.data.ConversationRepository
import com.github.maly7.data.UserRepository
import com.github.maly7.domain.User
import com.github.maly7.extensions.addContact
import com.github.maly7.extensions.createUser
import com.github.maly7.extensions.stubAuthUser
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate

open class ConversationSupport {
    val userToken = "user-token"
    val contactToken = "contact-token"
    val otherUserToken = "other-user"

    lateinit var currentUser: User
    lateinit var contact: User
    lateinit var otherUser: User

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var conversationRepository: ConversationRepository

    @Autowired
    lateinit var conversationMessageRepository: ConversationMessageRepository

    @Rule
    @JvmField
    val wireMock: WireMockRule = WireMockRule(AUTH_SERVICE_PORT)

    @Before
    open fun setup() {
        currentUser = restTemplate.createUser(User("user@email.com"))
        wireMock.stubAuthUser(userToken, currentUser)

        contact = restTemplate.createUser(User("contact@email.com"))
        wireMock.stubAuthUser(contactToken, contact)

        restTemplate.addContact(userToken, contact)

        otherUser = restTemplate.createUser(User("other@email.com"))
        wireMock.stubAuthUser(otherUserToken, otherUser)
    }

    @After
    fun cleanup() {
        conversationMessageRepository.deleteAll()
        conversationRepository.deleteAll()
        userRepository.deleteAll()
    }
}
