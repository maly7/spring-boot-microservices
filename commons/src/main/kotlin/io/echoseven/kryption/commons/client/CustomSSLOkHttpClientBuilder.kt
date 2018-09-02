package io.echoseven.kryption.commons.client

import io.echoseven.kryption.commons.getTrustManager
import okhttp3.OkHttpClient.Builder
import org.springframework.cloud.commons.httpclient.DefaultOkHttpClientFactory
import javax.net.ssl.SSLContext

class TrustingSSLOkHttpClientFactory(
    private val builder: Builder,
    private val trustStoreLocation: String,
    private val trustStorePassword: String
) : DefaultOkHttpClientFactory(builder) {
    override fun createBuilder(disableSslValidation: Boolean): Builder {
        if (disableSslValidation) {
            return super.createBuilder(disableSslValidation)
        }

        val trustManager = getTrustManager(trustStoreLocation, trustStorePassword)
        return builder.sslSocketFactory(SSLContext.getDefault().socketFactory, trustManager)
    }
}
