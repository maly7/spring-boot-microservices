package io.echoseven.kryption.web

import com.beust.klaxon.Klaxon
import io.echoseven.kryption.data.UserAccountRepository
import io.echoseven.kryption.domain.UserAccount
import io.echoseven.kryption.tokens.TokenIssuer
import io.echoseven.kryption.web.resource.TokenResource
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

    @Autowired
    lateinit var tokenIssuer: TokenIssuer

    private val userEmail = "login-user@email.com"
    private val userPassword = "login-password"

    @Before
    fun setup() {
        restTemplate.postForEntity("/user", UserAccount(userEmail, userPassword), String::class.java)
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

    @Test
    fun `Attempting to login with a bad password should fail`() {
        val failedResponse = restTemplate.postForEntity("/login", UserAccount(userEmail, "bad password"), String::class.java)

        assertTrue(failedResponse.statusCode.is4xxClientError, "The login request should fail")
        assertEquals(HttpStatus.UNAUTHORIZED, failedResponse.statusCode)
    }

    @Test
    fun `Using the right credentials should login successfully`() {
        val successfulResponse = restTemplate.postForEntity("/login", UserAccount(userEmail, userPassword), String::class.java)

        assertTrue(successfulResponse.statusCode.is2xxSuccessful, "The login request should succeed")
        assertEquals(HttpStatus.OK, successfulResponse.statusCode)

        val token: String = Klaxon().parse<TokenResource>(successfulResponse.body!!)?.token ?: ""

        assertEquals(userEmail, tokenIssuer.getEmailFromToken(token))
    }
}
