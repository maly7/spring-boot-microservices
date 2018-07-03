package io.echoseven.kryption.support

import io.echoseven.kryption.WithAuthenticatedUser
import io.echoseven.kryption.security.AuthenticationToken
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.test.context.support.WithSecurityContextFactory
import java.util.UUID

class WithAuthenticatedUserSecurityContextFactory : WithSecurityContextFactory<WithAuthenticatedUser> {
    override fun createSecurityContext(withUser: WithAuthenticatedUser): SecurityContext {
        val auth = AuthenticationToken(mutableListOf())
        auth.token = "${UUID.randomUUID()}"
        auth.id = withUser.username

        val context = SecurityContextHolder.createEmptyContext()
        context.authentication = auth
        return context
    }
}
