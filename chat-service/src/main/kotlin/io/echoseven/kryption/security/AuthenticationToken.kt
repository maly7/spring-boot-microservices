package io.echoseven.kryption.security

import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.authentication.AuthenticationServiceException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class AuthenticationToken(authorities: MutableCollection<out GrantedAuthority>?) : AbstractAuthenticationToken(authorities) {
    var token: String = ""
    var id: String = ""
    var userDetails: UserDetails? = null

    override fun getCredentials(): Any {
        return token
    }

    override fun getPrincipal(): Any {
        return userDetails
            ?: throw AuthenticationServiceException("Either the user was not full authenticated before retrieving details or we couldn't look them up")
    }

    override fun toString(): String {
        return "AuthenticationToken(token='$token', id='$id', userDetails=$userDetails)"
    }
}
