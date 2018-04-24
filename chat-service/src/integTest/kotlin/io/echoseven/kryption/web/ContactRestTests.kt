package io.echoseven.kryption.web

import com.github.tomakehurst.wiremock.junit.WireMockRule
import io.echoseven.kryption.ChatIntegrationTest
import io.echoseven.kryption.data.UserRepository
import io.echoseven.kryption.domain.User
import io.echoseven.kryption.support.AUTH_SERVICE_PORT
import io.echoseven.kryption.support.authHeaders
import io.echoseven.kryption.support.containsValue
import io.echoseven.kryption.support.countValue
import io.echoseven.kryption.support.createUser
import io.echoseven.kryption.support.stubAuthUser
import io.echoseven.kryption.web.resource.ContactRequest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit4.SpringRunner
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@Suppress("UNCHECKED_CAST")
@RunWith(SpringRunner::class)
@ChatIntegrationTest
class ContactRestTests {
    val userToken = "user-token"
    lateinit var currentUser: User

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Autowired
    lateinit var userRepository: UserRepository

    @Rule
    @JvmField
    final val wireMock: WireMockRule = WireMockRule(AUTH_SERVICE_PORT)

    @Before
    fun setup() {
        currentUser = createUser(User("user@email.com"), restTemplate)
        stubAuthUser(wireMock, userToken, currentUser)
    }

    @After
    fun cleanup() {
        userRepository.deleteAll()
    }

    @Test
    fun `A User Should be able to add a Contact`() {
        val contact = createUser(User("contact@email.com"), restTemplate)
        val contactRequestEntity = HttpEntity(ContactRequest(contact.email), authHeaders(userToken))
        val contactResponse = restTemplate.exchange("/contacts", HttpMethod.POST, contactRequestEntity, List::class.java)
        val contacts = contactResponse.body!! as List<LinkedHashMap<*, *>>

        assertEquals(HttpStatus.OK, contactResponse.statusCode, "The status code should be 200 successful")
        assertTrue(contacts.containsValue("email", contact.email), "The contact email should be in the list of returned contacts")
        assertEquals(1, contacts.countValue("email", contact.email), "There should be only one contact with that email")
    }

    @Test
    fun `A User should be able to retrieve their Contacts`() {
    }

    @Test
    fun `Attempting to add a non-existent user as a contact should fail`() {
    }

    @Test
    fun `Contact lists should not contain duplicates`() {
    }

    @Test
    fun `A User should be able to delete a Contact`() {
    }
}
