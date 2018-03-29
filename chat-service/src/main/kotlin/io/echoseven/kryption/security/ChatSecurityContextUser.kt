package io.echoseven.kryption.security

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class ChatSecurityContextUser(var token: String, var email: String, var userName: String) : Authentication, UserDetails {
    private var authenticated: Boolean = true

    override fun getUsername(): String = email

    override fun getName(): String = userName

    override fun getPrincipal(): Any = this

    override fun getDetails(): Any = emptyMap<String, String>()

    override fun getCredentials(): String = token

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = mutableListOf()

    override fun isCredentialsNonExpired(): Boolean = true

    override fun isEnabled(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true

    override fun isAccountNonExpired(): Boolean = true

    override fun isAuthenticated(): Boolean = authenticated

    override fun setAuthenticated(isAuthenticated: Boolean) {
        this.authenticated = isAuthenticated
    }

    override fun getPassword(): String = ""
}
