package io.echoseven.kryption.security

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy

@Configuration
class SecurityConfig : WebSecurityConfigurerAdapter() {

    @Autowired
    lateinit var tokenAuthenticationFilter: TokenAuthenticationFilter

    override fun configure(http: HttpSecurity?) {
        http!!

        http
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests().anyRequest().authenticated()
                .antMatchers("/actuator/**").permitAll()
                .and().addFilter(tokenAuthenticationFilter).anonymous().disable()
    }

    @Bean
    fun tokenAuthenticationFilter(): TokenAuthenticationFilter {
        var tokenFilter = TokenAuthenticationFilter()
        tokenFilter.setCheckForPrincipalChanges(true)
        return tokenFilter
    }
}
