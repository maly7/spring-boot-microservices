package io.echoseven.kryption.web

import io.echoseven.kryption.IntegrationTest
import io.echoseven.kryption.data.UserAccountRepository
import io.echoseven.kryption.domain.UserAccount
import io.echoseven.kryption.web.resource.UserAccountResource
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.junit4.SpringRunner
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertNotEquals
import kotlin.test.assertTrue

@RunWith(SpringRunner::class)
@IntegrationTest
class UserAccountRestTests {

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Autowired
    lateinit var userAccountRepository: UserAccountRepository

    @Autowired
    lateinit var passwordEncoder: PasswordEncoder

    @After
    fun cleanup() {
        userAccountRepository.deleteAll()
    }

    @Test
    fun `A POST to the user endpoint should create a new User Account`() {
        val userToCreate = UserAccount("email@foo.com", "password")
        val response = restTemplate.postForEntity("/user", userToCreate, UserAccountResource::class.java)

        assertTrue(response.statusCode.is2xxSuccessful, "The response status should be 200 successful")
        assertEquals(response.statusCode, HttpStatus.CREATED)

        val createdUser = response.body!!
        assertEquals(userToCreate.email, createdUser.email)
        assertEquals(userToCreate.isVerified, createdUser.isVerified)
    }

    @Test
    fun `Attempting to create the same user account twice should fail`() {
        val userToCreate = UserAccount("email@foo.com", "password")
        val response = restTemplate.postForEntity("/user", userToCreate, UserAccountResource::class.java)

        assertTrue(response.statusCode.is2xxSuccessful, "The first user create should succeed")
        assertEquals(response.statusCode, HttpStatus.CREATED)

        val sameEmailUser = UserAccount("email@foo.com", "different password")
        val failedResponse = restTemplate.postForEntity("/user", sameEmailUser, String::class.java)
        assertEquals(HttpStatus.BAD_REQUEST, failedResponse.statusCode, "We should not be able to create the same user twice")
    }


    @Test
    fun `user account passwords should be encoded`() {
        val userToCreate = UserAccount("email@foo.com", "password")
        val response = restTemplate.postForEntity("/user", userToCreate, UserAccountResource::class.java)

        assertTrue(response.statusCode.is2xxSuccessful, "The user create should succeed")

        val body = Optional.ofNullable(response.body)
        val repositoryUser = userAccountRepository.findById(body.get().id).get()

        assertNotEquals(userToCreate.password, repositoryUser.password, "The stored password should not match the input password")
        assertTrue(passwordEncoder.matches(userToCreate.password, repositoryUser.password), "The password should match the encoded version")
    }
}
