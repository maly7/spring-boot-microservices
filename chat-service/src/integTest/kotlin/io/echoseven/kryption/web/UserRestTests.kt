package io.echoseven.kryption.web

import io.echoseven.kryption.data.UserRepository
import io.echoseven.kryption.domain.User
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@RunWith(SpringRunner::class)
@ActiveProfiles("local", "integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserRestTests {

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Autowired
    lateinit var userRepository: UserRepository

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
    fun `Attempting to create the same user twice should fail`() {
        val userToCreate = User("email@foo.com")

        val response = restTemplate.postForEntity("/user", userToCreate, User::class.java)

        assertTrue(response.statusCode.is2xxSuccessful, "The response status should be 200 successful the first time")
        assertEquals(response.statusCode, HttpStatus.CREATED)

        val failedResponse = restTemplate.postForEntity("/user", userToCreate, User::class.java)
        assertTrue(failedResponse.statusCode.is4xxClientError, "We should not be able to create the user twice")
    }
}
