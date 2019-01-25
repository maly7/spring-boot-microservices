package com.github.maly7

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.cloud.client.discovery.EnableDiscoveryClient
import org.springframework.cloud.netflix.eureka.EnableEurekaClient
import org.springframework.cloud.openfeign.EnableFeignClients
import org.springframework.data.mongodb.config.EnableMongoAuditing
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity

@EnableEurekaClient
@EnableDiscoveryClient
@EnableFeignClients
@EnableMongoAuditing
@SpringBootApplication
@EnableGlobalMethodSecurity(prePostEnabled = true)
class ChatApp

fun main(args: Array<String>) {
    runApplication<ChatApp>(*args)
}
