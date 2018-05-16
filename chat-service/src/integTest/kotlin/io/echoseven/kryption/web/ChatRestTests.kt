package io.echoseven.kryption.web

import com.github.tomakehurst.wiremock.junit.WireMockRule
import io.echoseven.kryption.ChatIntegrationTest
import io.echoseven.kryption.data.ChatMessageRepository
import io.echoseven.kryption.data.ChatRepository
import io.echoseven.kryption.data.UserRepository
import io.echoseven.kryption.domain.User
import io.echoseven.kryption.extensions.addContact
import io.echoseven.kryption.extensions.createUser
import io.echoseven.kryption.extensions.sendMessage
import io.echoseven.kryption.extensions.stubAuthUser
import io.echoseven.kryption.support.AUTH_SERVICE_PORT
import org.hamcrest.Matchers.hasSize
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit4.SpringRunner
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

@RunWith(SpringRunner::class)
@ChatIntegrationTest
class ChatRestTests {
    val userToken = "user-token"
    val contactToken = "contact-token"
    lateinit var currentUser: User
    lateinit var contact: User

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var chatRepository: ChatRepository

    @Autowired
    lateinit var chatMessageRepository: ChatMessageRepository

    @Rule
    @JvmField
    final val wireMock: WireMockRule = WireMockRule(AUTH_SERVICE_PORT)

    @Before
    fun setup() {
        currentUser = restTemplate.createUser(User("user@email.com"))
        wireMock.stubAuthUser(userToken, currentUser)

        contact = restTemplate.createUser(User("contact@email.com"))
        wireMock.stubAuthUser(contactToken, contact)

        restTemplate.addContact(userToken, contact)
    }

    @After
    fun cleanup() {
        chatMessageRepository.deleteAll()
        chatRepository.deleteAll()
        userRepository.deleteAll()
    }

    @Test
    fun `Users should be able to send messages`() {
        val messageText = "Initial Message"
        val response = restTemplate.sendMessage(userToken, contact.id!!, messageText)
        val chat = response.body!!

        assertEquals(HttpStatus.OK, response.statusCode, "The status code should be 200 successful")
        assertThat("There should be only one message", chat.messages, hasSize(1))
        val firstMessage = chat.messages.first()

        assertEquals(messageText, firstMessage.message, "The message text is $messageText")
        assertEquals(currentUser.id, firstMessage.fromId, "The from id should be from the sending user")
        assertEquals(contact.id, firstMessage.toId, "The to id should be the contact")
        assertNotNull(firstMessage.timestamp, "There should be a timestamp")

        val reply = "Reply message"
        val replyResponse = restTemplate.sendMessage(contactToken, currentUser.id!!, reply)
        val replyChat = replyResponse.body!!

        assertEquals(HttpStatus.OK, replyResponse.statusCode, "A user should be able to reply")
        assertThat("There should now be two messages in the chat", replyChat.messages, hasSize(2))

        val replyMessage = replyChat.messages.last()
        assertEquals(reply, replyMessage.message, "The message text is $reply")
        assertEquals(currentUser.id, replyMessage.toId, "The to id should be the first user")
        assertEquals(contact.id, replyMessage.fromId, "The from id should be the contact")
        assertNotNull(replyMessage.timestamp, "There should be a timestamp")
    }

    @Test
    fun `Users should not be able to initiate chats to non-contacts`() {
    }
}
