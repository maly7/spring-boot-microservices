package io.echoseven.kryption.security

import org.springframework.http.HttpHeaders
import org.springframework.security.web.authentication.preauth.AbstractPreAuthenticatedProcessingFilter
import javax.servlet.http.HttpServletRequest

class TokenAuthenticationFilter : AbstractPreAuthenticatedProcessingFilter() {
    override fun getPreAuthenticatedCredentials(request: HttpServletRequest?): Any {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getPreAuthenticatedPrincipal(request: HttpServletRequest?): Any {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun authenticate(request: HttpServletRequest): Any {
        var token = request.getHeader(HttpHeaders.AUTHORIZATION)


    }
}
