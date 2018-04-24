package io.echoseven.kryption.web

import com.github.tomakehurst.wiremock.junit.WireMockRule
import io.echoseven.kryption.ChatIntegrationTest
import io.echoseven.kryption.data.UserRepository
import io.echoseven.kryption.domain.User
import io.echoseven.kryption.support.AUTH_SERVICE_PORT
import io.echoseven.kryption.support.authHeaders
import io.echoseven.kryption.support.createUser
import io.echoseven.kryption.support.stubAuthUser
import io.echoseven.kryption.web.resource.ContactRequest
import org.junit.After
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

@RunWith(SpringRunner::class)
@ChatIntegrationTest
class ContactRestTests {
    val userToken = "user-token"

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Autowired
    lateinit var userRepository: UserRepository

    @Rule
    @JvmField
    final val wireMock: WireMockRule = WireMockRule(AUTH_SERVICE_PORT)

    @After
    fun cleanup() {
        userRepository.deleteAll()
    }

    @Test
    fun `A User Should be able to add a Contact`() {
        val contact = createUser(User("contact@email.com"), restTemplate)
        val user = createUser(User("user@email.com"), restTemplate)

        stubAuthUser(wireMock, userToken, user)

        val contactRequestEntity = HttpEntity(ContactRequest(contact.email), authHeaders(userToken))
        val contactResponse = restTemplate.exchange("/contacts", HttpMethod.POST, contactRequestEntity, List::class.java)

        assertEquals(HttpStatus.OK, contactResponse.statusCode, "The status code should be 200 successful")

        assertTrue(contactResponse.body!!.any {
            (it as Map<*, *>)["email"] == contact.email
        }, "The contact should be in the list of returned contacts")

        assertEquals(1, contactResponse.body!!.count {
            (it as Map<*, *>)["email"] == contact.email
        }, "There should be only one contact with that email")
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
