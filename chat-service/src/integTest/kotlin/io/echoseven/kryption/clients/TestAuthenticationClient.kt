package io.echoseven.kryption.clients

import org.springframework.stereotype.Component

@Component
class TestAuthenticationClient : AuthenticationClient {
    val users: HashMap<String, AuthUser> = HashMap()

    override fun authenticate(token: String): AuthUser {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}