package io.echoseven.kryption.security

import io.echoseven.kryption.clients.AuthenticationClient
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy

@Configuration
class SecurityConfig : WebSecurityConfigurerAdapter() {

    @Autowired
    lateinit var authenticationProvider: JwtAuthenticationProvider

    @Autowired
    lateinit var authenticationClient: AuthenticationClient

    override fun configure(http: HttpSecurity) {
        http
            .csrf().disable()
            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            .and().authorizeRequests()
            .antMatchers("/actuator/**").permitAll()
            .antMatchers("/user/**").permitAll()
            .anyRequest().authenticated()
            .and().addFilter(JwtAuthorizationFilter(authenticationManager(), authenticationClient)).anonymous().disable()
    }

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.authenticationProvider(authenticationProvider)
    }
}
