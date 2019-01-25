package com.github.maly7.web

import com.github.tomakehurst.wiremock.junit.WireMockRule
import com.github.maly7.ChatIntegrationTest
import com.github.maly7.data.UserRepository
import com.github.maly7.domain.User
import com.github.maly7.extensions.createUser
import com.github.maly7.extensions.getForEntity
import com.github.maly7.extensions.stubAuthUser
import com.github.maly7.support.AUTH_SERVICE_PORT
import com.github.maly7.support.authHeaders
import org.junit.After
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
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
    final val wireMock: WireMockRule = WireMockRule(AUTH_SERVICE_PORT)

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
        assertEquals(HttpStatus.BAD_REQUEST, failedResponse.statusCode, "We should not be able to create the user twice")
    }

    @Test
    fun `Only an authenticated user should be able to request their own user info`() {
        val token = "foo-user"
        val originalUser = restTemplate.createUser(User("email@foo.com"))
        wireMock.stubAuthUser(token, originalUser)

        val userLookupResponse: User = restTemplate.getForEntity("/user", authHeaders(token), User::class.java).body!!

        assertEquals(originalUser, userLookupResponse, "The retrieved user should be the same as the created user")

        val secondUser = restTemplate.createUser(User("second.user@foo.com"))
        val secondToken = "other-user"
        wireMock.stubAuthUser(secondToken, secondUser)

        val secondUserLookup = restTemplate.getForEntity("/user", authHeaders(secondToken), User::class.java).body!!

        assertEquals(secondUser, secondUserLookup, "The second user should match the second created user")
        assertNotEquals(userLookupResponse, secondUserLookup, "The two fetched users should not match")
    }
}
