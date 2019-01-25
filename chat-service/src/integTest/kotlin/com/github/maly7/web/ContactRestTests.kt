package com.github.maly7.web

import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.github.maly7.ChatIntegrationTest
import com.github.maly7.data.UserRepository
import com.github.maly7.domain.User
import com.github.maly7.extensions.addContact
import com.github.maly7.extensions.containsEmail
import com.github.maly7.extensions.countEmail
import com.github.maly7.extensions.createUser
import com.github.maly7.extensions.createUserAsContact
import com.github.maly7.extensions.deleteContact
import com.github.maly7.extensions.getContacts
import com.github.maly7.extensions.getForEntity
import com.github.maly7.extensions.stubAuthUser
import com.github.maly7.support.AUTH_SERVICE_PORT
import com.github.maly7.support.authHeaders
import com.github.maly7.web.resource.ContactRequest
import org.hamcrest.Matchers.hasSize
import org.junit.After
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpEntity
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit4.SpringRunner
import kotlin.test.assertEquals
import kotlin.test.assertFalse
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
        currentUser = restTemplate.createUser(User("user@email.com"))
        wireMock.stubAuthUser(userToken, currentUser)
    }

    @After
    fun cleanup() {
        userRepository.deleteAll()
    }

    @Test
    fun `A User Should be able to add a Contact`() {
        val contact = restTemplate.createUser(User("contact@email.com"))
        val contactRequestEntity = HttpEntity(ContactRequest(contact.email), authHeaders(userToken))
        val contactResponse = restTemplate.postForEntity("/contacts", contactRequestEntity, List::class.java)
        val contacts = contactResponse.body!! as List<LinkedHashMap<*, *>>

        assertEquals(HttpStatus.OK, contactResponse.statusCode, "The status code should be 200 successful")
        assertTrue(contacts.containsEmail(contact.email), "The contact email should be in the list of returned contacts")
        assertEquals(1, contacts.countEmail(contact.email), "There should be only one contact with that email")
    }

    @Test
    fun `A User should be able to retrieve their Contacts`() {
        val addedContacts = mutableListOf<String>()
        for (i in 1..5) {
            addedContacts.add(restTemplate.createUserAsContact(userToken))
        }

        val contactsResponse = restTemplate.getContacts(userToken)
        val contacts = contactsResponse.body!! as List<LinkedHashMap<*, *>>

        addedContacts.forEach { contact ->
            assertTrue(contacts.containsEmail(contact), "$contact should be in the list of returned contacts")
            assertEquals(1, contacts.countEmail(contact), "There should be only one of $contact in the list of contacts")
        }
        assertThat("There should be no excess contacts", contacts, hasSize(5))
    }

    @Test
    fun `Attempting to add a non-existent user as a contact should fail`() {
        val badEmail = "non-existent@email.com"
        val response = restTemplate.addContact(userToken, User(badEmail))

        assertEquals(HttpStatus.BAD_REQUEST, response.statusCode, "The response status should be ${HttpStatus.BAD_REQUEST.value()}")

        val contacts = restTemplate.getContacts(userToken).body!! as List<LinkedHashMap<*, *>>

        assertFalse(contacts.containsEmail(badEmail), "The non-existent user email should not be in the contacts list")
    }

    @Test
    fun `Contact lists should not contain duplicates`() {
        val contact = restTemplate.createUser(User("other-user@email.com"))

        for (i in 1..5) {
            restTemplate.addContact(userToken, contact)
        }

        val contactsResponse = restTemplate.getForEntity("/contacts", authHeaders(userToken), List::class.java)
        val contacts = contactsResponse.body!! as List<LinkedHashMap<*, *>>

        assertThat("There should only be one contact", contacts, hasSize(1))
    }

    @Test
    fun `A User should be able to delete a Contact`() {
        val contactEmail = restTemplate.createUserAsContact(userToken)

        var contacts = restTemplate.getContacts(userToken).body!! as List<LinkedHashMap<*, *>>
        assertTrue(contacts.containsEmail(contactEmail), "$contactEmail should be in the list of returned contacts")

        restTemplate.deleteContact(userToken, contactEmail)

        contacts = restTemplate.getContacts(userToken).body!! as List<LinkedHashMap<*, *>>
        assertFalse(contacts.containsEmail(contactEmail), "$contactEmail should have been deleted from the contact list")
    }

    @Test
    fun `Contacts should be unidirectional`() {
        val secondUser = restTemplate.createUser(User("other-user@email.org"))
        val secondToken = "other-user"
        wireMock.stubAuthUser(secondToken, secondUser)

        restTemplate.addContact(userToken, secondUser)
        val firstUserContacts = restTemplate.getContacts(userToken).body!! as List<LinkedHashMap<*, *>>
        assertTrue(firstUserContacts.containsEmail(secondUser.email), "The original user should have the second user as a contact")

        val secondUserContacts = restTemplate.getContacts(secondToken).body!! as List<LinkedHashMap<*, *>>
        assertFalse(secondUserContacts.containsEmail(currentUser.email), "The second user should not have the original user as a contact")
    }
}
