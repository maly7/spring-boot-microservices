package io.echoseven.kryption.security

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
    lateinit var tokenAuthenticationFilter: TokenAuthenticationFilter

    @Autowired
    lateinit var authenticationProvider: ChatUserAuthenticationProvider

    override fun configure(http: HttpSecurity?) {
        http!!

        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests().anyRequest().authenticated()
                .antMatchers("/actuator/**").permitAll()
                .antMatchers(HttpMethod.POST, "/user/**").permitAll()
                .and().addFilter(tokenAuthenticationFilter).anonymous().disable()
    }

    override fun configure(auth: AuthenticationManagerBuilder?) {
        auth?.authenticationProvider(authenticationProvider)
    }
}
