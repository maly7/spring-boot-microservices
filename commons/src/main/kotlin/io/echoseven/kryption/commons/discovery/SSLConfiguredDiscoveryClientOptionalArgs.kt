package io.echoseven.kryption.commons.discovery

import com.netflix.discovery.DiscoveryClient
import com.netflix.discovery.shared.transport.jersey.EurekaJerseyClientImpl

class SSLConfiguredDiscoveryClientOptionalArgs(
    clientName: String = "registry-client",
    maxConnectionsPerHost: Int = 10,
    maxTotalConnections: Int = 10
) :
    DiscoveryClient.DiscoveryClientOptionalArgs() {
    init {
        setEurekaJerseyClient(
            EurekaJerseyClientImpl.EurekaJerseyClientBuilder()
                .withSystemSSLConfiguration()
                .withClientName(clientName)
                .withMaxConnectionsPerHost(maxConnectionsPerHost)
                .withMaxTotalConnections(maxTotalConnections)
                .build()
        )
    }
}
