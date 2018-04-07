package io.echoseven.kryption.web

import com.github.tomakehurst.wiremock.junit.WireMockRule
import io.echoseven.kryption.ChatIntegrationTest
import io.echoseven.kryption.data.UserRepository
import io.echoseven.kryption.domain.User
import io.echoseven.kryption.support.createUser
import io.echoseven.kryption.support.stubAuthUser
import io.echoseven.kryption.support.tokenHeaders
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
import kotlin.test.assertFalse
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

@RunWith(SpringRunner::class)
@ChatIntegrationTest
class UserRestTests {

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Autowired
    lateinit var userRepository: UserRepository

    @Rule
    @JvmField
    final val wireMock: WireMockRule = WireMockRule(8089)

    @After
    fun cleanup() {
        userRepository.deleteAll()
    }

    @Test
    fun `A POST to the user endpoint should create a new chat user`() {
        val userToCreate = User("email@foo.com")

        val response = restTemplate.postForEntity("/user", userToCreate, User::class.java)

        assertTrue(response.statusCode.is2xxSuccessful, "The response status should be 200 successful")
        assertEquals(response.statusCode, HttpStatus.CREATED)

        val createdUser = response.body!!
        assertEquals(userToCreate.email, createdUser.email, "The email should be set")

        val foundUser = userRepository.findById(createdUser.id!!).get()
        assertEquals(foundUser, createdUser, "The response should match what's in the database")
    }

    @Test
    fun `Delete an existing user by id`() {
        val userToCreate = User("email@foo.com")
        val response = restTemplate.postForEntity("/user", userToCreate, User::class.java)
        assertTrue(response.statusCode.is2xxSuccessful, "The user should exist prior to deleting")

        restTemplate.delete("/user/{id}", userToCreate.id)

        val emptyUser = userRepository.findById(response.body!!.id!!)
        assertFalse(emptyUser.isPresent, "The user should no longer be found by the repository")
    }

    @Test
    fun `Attempting to create the same user twice should fail`() {
        val userToCreate = User("email@foo.com")

        val response = restTemplate.postForEntity("/user", userToCreate, User::class.java)

        assertTrue(response.statusCode.is2xxSuccessful, "The response status should be 200 successful the first time")
        assertEquals(response.statusCode, HttpStatus.CREATED)

        val failedResponse = restTemplate.postForEntity("/user", userToCreate, String::class.java)
        assertTrue(failedResponse.statusCode.is4xxClientError, "We should not be able to create the user twice")
    }

    @Test
    fun `Only an authenticated user should be able to request their own user info`() {
        val token = "foo-user"
        val originalUser = createUser(User("email@foo.com"), restTemplate)
        stubAuthUser(wireMock, token, originalUser)

        val entity = HttpEntity("", tokenHeaders(token))
        val userLookupResponse: User = restTemplate.exchange("/user", HttpMethod.GET, entity, User::class.java).body!!

        assertEquals(originalUser, userLookupResponse, "The retrieved user should be the same as the created user")

        val secondUser = createUser(User("second.user@foo.com"), restTemplate)
        val secondToken = "other-user"
        stubAuthUser(wireMock, secondToken, secondUser)

        val secondEntity = HttpEntity("", tokenHeaders(secondToken))
        val secondUserLookup = restTemplate.exchange("/user", HttpMethod.GET, secondEntity, User::class.java).body!!

        assertEquals(secondUser, secondUserLookup, "The second user should match the second created user")
        assertNotEquals(userLookupResponse, secondUserLookup, "The two fetched users should not match")
    }
}
