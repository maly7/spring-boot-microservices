package io.echoseven.kryption.security

import io.echoseven.kryption.clients.AuthenticationClient
import org.slf4j.LoggerFactory
import org.springframework.http.HttpHeaders
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthorizationFilter(
    authenticationManager: AuthenticationManager?,
    private val authenticationClient: AuthenticationClient
) : BasicAuthenticationFilter(authenticationManager) {

    private val log = LoggerFactory.getLogger(JwtAuthorizationFilter::class.java)

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        val token: String? = request.getHeader(HttpHeaders.AUTHORIZATION)

        if (token == null || token.isBlank()) {
            chain.doFilter(request, response)
            return
        }

        authenticate(token)
        chain.doFilter(request, response)
    }

    private fun authenticate(token: String) {
        val authUser = authenticationClient.authenticate(token)

        val auth = AuthenticationToken(mutableListOf())
        auth.token = token
        auth.id = authUser.id
        log.debug("Successfully filtered authentication for [{}]", auth.token)

        SecurityContextHolder.getContext().authentication = auth
    }
}
