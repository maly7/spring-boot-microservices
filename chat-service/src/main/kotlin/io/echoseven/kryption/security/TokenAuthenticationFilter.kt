package io.echoseven.kryption.security

import io.echoseven.kryption.clients.AuthenticationClient
import io.echoseven.kryption.data.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpHeaders
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter
import javax.servlet.http.HttpServletRequest

class TokenAuthenticationFilter : AbstractPreAuthenticatedProcessingFilter() {

    @Autowired
    lateinit var authenticationClient: AuthenticationClient

    @Autowired
    lateinit var userRepository: UserRepository

    override fun getPreAuthenticatedCredentials(request: HttpServletRequest?): Any = authenticate(request!!).credentials

    override fun getPreAuthenticatedPrincipal(request: HttpServletRequest?): Any = authenticate(request!!).principal

    private fun authenticate(request: HttpServletRequest): ChatSecurityContextUser {
        val token = request.getHeader(HttpHeaders.AUTHORIZATION)
                ?: throw UsernameNotFoundException("No authorization token provided")

        val authUser = authenticationClient.authenticate(token)
        val appUser = userRepository.findById(authUser.id).orElseThrow {
            UsernameNotFoundException("No User found for provided token with id [${authUser.id}]")
        }

        val chatUser = ChatSecurityContextUser(token, appUser.email, appUser.email)

        SecurityContextHolder.getContext().authentication = chatUser
        return chatUser
    }
}
