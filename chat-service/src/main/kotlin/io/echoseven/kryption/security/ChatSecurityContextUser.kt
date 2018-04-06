package io.echoseven.kryption.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

data class ChatSecurityContextUser(val email: String,
                                   private val authorities: MutableCollection<out GrantedAuthority>) : UserDetails {

    var id: String = ""
    var name: String = ""

    override fun getUsername() = email

    override fun getAuthorities() = authorities

    override fun isEnabled() = true

    override fun isCredentialsNonExpired() = true

    override fun getPassword() = ""

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

}
