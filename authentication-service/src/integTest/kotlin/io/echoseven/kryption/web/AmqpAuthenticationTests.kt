package io.echoseven.kryption.web

import io.echoseven.kryption.AuthIntegrationTest
import io.echoseven.kryption.domain.UserAccount
import io.echoseven.kryption.web.resource.TokenResource
import io.echoseven.kryption.web.resource.UserAccountResource
import io.echoseven.kryption.web.resource.amqp.ALLOW_ACCESS
import io.echoseven.kryption.web.resource.amqp.DENY_ACCESS
import io.echoseven.kryption.web.resource.amqp.TopicCheck
import io.echoseven.kryption.web.resource.amqp.VirtualHostCheck
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.http.HttpStatus
import org.springframework.test.context.junit4.SpringRunner
import java.util.UUID
import kotlin.test.assertEquals

@AuthIntegrationTest
@RunWith(SpringRunner::class)
class AmqpAuthenticationTests {

    lateinit var token: String
    val email = "amqp@test.com"

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Before
    fun setup() {
        val userAccount = UserAccount(email, "amqp-test")
        restTemplate.postForEntity("/user", userAccount, UserAccountResource::class.java).body!!
        token = restTemplate.postForEntity("/login", userAccount, TokenResource::class.java).body!!.token
    }

    @Test
    fun `Vhost requests should always non-blank usernames`() {
        val check = VirtualHostCheck()
        check.vhost = "/"
        check.username = email
        val response = restTemplate.postForEntity("/amqp/vhost", check, String::class.java)

        assertEquals(HttpStatus.OK, response.statusCode, "The response should be 200 OK")
        assertEquals(ALLOW_ACCESS, response.body, "The user should be allowed access")
    }

    @Test
    fun `Topic requests should always deny`() {
        val check = TopicCheck("user.*")
        check.vhost = "/"
        check.username = "${UUID.randomUUID()}"

        val response = restTemplate.postForEntity("/amqp/topic", check, String::class.java)
        assertEquals(HttpStatus.OK, response.statusCode, "The response should be 200 OK")
        assertEquals(DENY_ACCESS, response.body, "The user should be denies access")
    }
}
