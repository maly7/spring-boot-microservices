package io.echoseven.kryption.web

import io.echoseven.kryption.data.UserAccountRepository
import io.echoseven.kryption.domain.UserAccount
import org.junit.After
import org.junit.Before
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
class UserLoginTests {

    @Autowired
    lateinit var userAccountRepository: UserAccountRepository

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    lateinit var existingUser: UserAccount

    private val userEmail = "login-user@email.com"
    private val userPassword = "login-password"

    @Before
    fun setup() {
        existingUser = userAccountRepository.save(UserAccount(email = userEmail, password = userPassword))
    }

    @After
    fun cleanup() {
        userAccountRepository.deleteAll()
    }

    @Test
    fun `Attempting to login as a non-existing user should fail`() {
        val failedResponse = restTemplate.postForEntity("/login", UserAccount("bad-email", "does not matter"), String::class.java)

        assertTrue(failedResponse.statusCode.is4xxClientError, "The login request should fail")
        assertEquals(HttpStatus.BAD_REQUEST, failedResponse.statusCode)
    }
}
