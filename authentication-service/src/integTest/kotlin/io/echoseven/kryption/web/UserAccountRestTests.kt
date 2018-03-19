package io.echoseven.kryption.web

import io.echoseven.kryption.data.UserAccountRepository
import io.echoseven.kryption.domain.UserAccount
import io.echoseven.kryption.web.resource.UserAccountResource
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.ResponseEntity
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.junit4.SpringRunner
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

@RunWith(SpringRunner::class)
@ActiveProfiles("local", "integration")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserAccountRestTests {

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Autowired
    lateinit var userAccountRepository: UserAccountRepository

    @After
    fun cleanup() {
        userAccountRepository.deleteAll()
    }

    @Test
    fun `A POST to the user endpoint should create a new User Account`() {
        val userToCreate = UserAccount("email@foo.com", "password")
        val response = restTemplate.postForEntity("/user", userToCreate, UserAccountResource::class.java)

        assertTrue(response.statusCode.is2xxSuccessful, "The response status should be 200 successful")

        val createdUser = response.body!!
        assertEquals(userToCreate.email, createdUser.email)
        assertEquals(userToCreate.isVerified, createdUser.isVerified)
    }
}
