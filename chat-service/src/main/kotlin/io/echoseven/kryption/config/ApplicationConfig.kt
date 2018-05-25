package io.echoseven.kryption.config

import io.echoseven.kryption.properties.MessagingProperties
import org.springframework.amqp.core.AmqpAdmin
import org.springframework.amqp.core.FanoutExchange
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
    fun exchangeCreator(amqpAdmin: AmqpAdmin, messagingProperties: MessagingProperties): InitializingBean =
        InitializingBean {
            amqpAdmin.declareExchange(FanoutExchange("user.updates"))
            amqpAdmin.declareQueue()
        }
}
