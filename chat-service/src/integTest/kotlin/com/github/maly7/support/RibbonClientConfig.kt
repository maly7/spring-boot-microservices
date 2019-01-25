package com.github.maly7.support

import com.netflix.loadbalancer.Server
import com.netflix.loadbalancer.ServerList
import org.springframework.cloud.netflix.ribbon.StaticServerList
import org.springframework.context.annotation.Bean

class RibbonClientConfig {
    @Bean
    fun ribbonServerList(): ServerList<Server> {
        return StaticServerList(Server("localhost", AUTH_SERVICE_PORT))
    }
}
