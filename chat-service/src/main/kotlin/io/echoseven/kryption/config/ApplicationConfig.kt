package io.echoseven.kryption.config

import io.echoseven.kryption.properties.MessagingProperties
import org.springframework.amqp.core.AmqpAdmin
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Exchange
import org.springframework.beans.factory.InitializingBean
import org.springframework.cloud.client.loadbalancer.LoadBalanced
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class ApplicationConfig {

    @Bean
    @LoadBalanced
    fun restTemplate(): RestTemplate = RestTemplate()

    @Bean
    fun userExchange(messagingProperties: MessagingProperties) = DirectExchange(messagingProperties.userExchange)

    @Bean
    fun exchangeCreator(amqpAdmin: AmqpAdmin, userExchange: Exchange): InitializingBean =
        InitializingBean {
            amqpAdmin.declareExchange(userExchange)
        }
}
