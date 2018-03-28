package io.echoseven.kryption.security

import org.springframework.http.HttpHeaders
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter
import javax.servlet.http.HttpServletRequest

class TokenAuthenticationFilter : AbstractPreAuthenticatedProcessingFilter() {
    override fun getPreAuthenticatedCredentials(request: HttpServletRequest?): Any = authenticate(request!!).credentials

    override fun getPreAuthenticatedPrincipal(request: HttpServletRequest?): Any = authenticate(request!!).principal

    private fun authenticate(request: HttpServletRequest): ChatUser {
        val token = request.getHeader(HttpHeaders.AUTHORIZATION)

        // look up user in auth service
        // fail if no user
        // set the security context

        return ChatUser(token, "", "")
    }
}
