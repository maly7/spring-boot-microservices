package io.echoseven.kryption.commons.client

import okhttp3.OkHttpClient
import org.springframework.cloud.commons.httpclient.DefaultOkHttpClientFactory
import javax.net.ssl.SSLContext

class CustomSSLOkHttpClientFactory(
    val builder: okhttp3.OkHttpClient.Builder,
    private val trustStoreLocation: String,
    private val trustStorePassword: String
) : DefaultOkHttpClientFactory(builder) {
    override fun createBuilder(disableSslValidation: Boolean): OkHttpClient.Builder {
        if (disableSslValidation) {
            return super.createBuilder(disableSslValidation)
        }

        if (System.getProperty("javax.net.ssl.trustStore").isBlank()) {
            System.setProperty("javax.net.ssl.trustStore", trustStoreLocation)
        }

        if (System.getProperty("javax.net.ssl.trustStorePassword").isBlank()) {
            System.setProperty("javax.net.ssl.trustStorePassword", trustStorePassword)
        }

        //TODO: remove usage of deprecated method
        return builder.sslSocketFactory(SSLContext.getDefault().socketFactory)
    }
}
