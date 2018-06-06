package io.echoseven.kryption.web

import io.echoseven.kryption.AuthIntegrationTest
import io.echoseven.kryption.domain.UserAccount
import io.echoseven.kryption.support.UserAccountCleanup
import io.echoseven.kryption.support.assertAllowedAccess
import io.echoseven.kryption.support.assertDeniedAccess
import io.echoseven.kryption.web.resource.TokenResource
import io.echoseven.kryption.web.resource.amqp.ResourceCheck
import io.echoseven.kryption.web.resource.amqp.TopicCheck
import io.echoseven.kryption.web.resource.amqp.VirtualHostCheck
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.web.client.TestRestTemplate
import org.springframework.test.context.junit4.SpringRunner
import java.util.UUID

@AuthIntegrationTest
@RunWith(SpringRunner::class)
class AmqpAuthenticationTests : UserAccountCleanup() {

    lateinit var token: String
    val email = "amqp@test.com"

    @Autowired
    lateinit var restTemplate: TestRestTemplate

    @Before
    fun setup() {
        val userAccount = UserAccount(email, "amqp-test")
        restTemplate.postForEntity("/user", userAccount, String::class.java).body!!
        token = restTemplate.postForEntity("/login", userAccount, TokenResource::class.java).body!!.token
    }

    @Test
    fun `User requests should allow valid usernames and tokens`() {
        val response =
            restTemplate.postForEntity("/amqp/user?username=$email&password=$token", null, String::class.java)
        response.assertAllowedAccess()
    }

    @Test
    fun `User requests should deny when the user cannot be found`() {
        val response = restTemplate.postForEntity("/amqp/user?username=foo&password=$token", null, String::class.java)
        response.assertDeniedAccess()
    }

    @Test
    fun `Vhost requests should allow non-blank usernames and vhosts`() {
        val check = VirtualHostCheck()
        check.vhost = "/"
        check.username = email
        val response = restTemplate.postForEntity("/amqp/vhost", check, String::class.java)

        response.assertAllowedAccess()
    }

    @Test
    fun `Vhost requests should deny when the username is empty`() {
        val check = VirtualHostCheck()
        check.vhost = "/"
        val response = restTemplate.postForEntity("/amqp/vhost", check, String::class.java)

        response.assertDeniedAccess()
    }

    @Test
    fun `Vhost requests should deny when the vhost is empty`() {
        val check = VirtualHostCheck()
        check.username = email
        val response = restTemplate.postForEntity("/amqp/vhost", check, String::class.java)

        response.assertDeniedAccess()
    }

    @Test
    fun `Resource requests should allow valid requests`() {
        val userId = userAccountRepository.findByEmail(email).get().id
        val check = ResourceCheck("queue", "user.$userId", "read")
        check.username = email
        check.vhost = "/"

        val response = restTemplate.postForEntity("/amqp/resource", check, String::class.java)
        response.assertAllowedAccess()
    }

    @Test
    fun `Resource requests should deny unsupported resource types`() {
        val userId = userAccountRepository.findByEmail(email).get().id
        val check = ResourceCheck("exchange", "user.$userId", "read")
        check.username = email
        check.vhost = "/"

        val response = restTemplate.postForEntity("/amqp/resource", check, String::class.java)
        response.assertDeniedAccess()
    }

    @Test
    fun `Resource requests should deny requests to other user queues`() {
        val userId = userAccountRepository.save(UserAccount("other@email.com", "${UUID.randomUUID()}")).id
        val check = ResourceCheck("queue", "user.$userId", "read")
        check.username = email
        check.vhost = "/"

        val response = restTemplate.postForEntity("/amqp/resource", check, String::class.java)
        response.assertDeniedAccess()
    }

    @Test
    fun `Topic requests should always deny`() {
        val check = TopicCheck("user.*")
        check.vhost = "/"
        check.username = "${UUID.randomUUID()}"
        val response = restTemplate.postForEntity("/amqp/topic", check, String::class.java)

        response.assertDeniedAccess()
    }
}