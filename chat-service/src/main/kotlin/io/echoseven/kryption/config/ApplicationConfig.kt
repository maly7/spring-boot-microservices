package io.echoseven.kryption.config

import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class ApplicationConfig {

    @Bean
    @LoadBalanced
    fun restTemplate(): RestTemplate = RestTemplate()
}