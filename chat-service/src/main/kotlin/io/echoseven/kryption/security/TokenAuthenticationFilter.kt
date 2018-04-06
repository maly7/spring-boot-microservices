package io.echoseven.kryption.security

import io.echoseven.kryption.clients.AuthenticationClient
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter
import javax.servlet.http.HttpServletRequest

class TokenAuthenticationFilter : AbstractPreAuthenticatedProcessingFilter() {
    private val log = LoggerFactory.getLogger(TokenAuthenticationFilter::class.java)

    @Autowired
    lateinit var authenticationClient: AuthenticationClient

    override fun getPreAuthenticatedCredentials(request: HttpServletRequest?): Any = authenticate(request!!)

    override fun getPreAuthenticatedPrincipal(request: HttpServletRequest?): Any = authenticate(request!!)

    private fun authenticate(request: HttpServletRequest) {
        val token = request.getHeader(HttpHeaders.AUTHORIZATION)
                ?: throw UsernameNotFoundException("No authorization token provided")

        val authUser = authenticationClient.authenticate(token)

        val auth = AuthenticationToken(mutableListOf(SimpleGrantedAuthority("Kryption-User")))
        auth.token = token
        auth.id = authUser.id
        log.debug("Successfully filtered authentication for [{}]", auth.token)

        SecurityContextHolder.getContext().authentication = auth
    }
}
