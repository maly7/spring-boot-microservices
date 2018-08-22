package io.echoseven.kryption.commons.client

import okhttp3.OkHttpClient.Builder
import org.springframework.cloud.commons.httpclient.DefaultOkHttpClientFactory
import javax.net.ssl.SSLContext

class CustomSSLOkHttpClientFactory(
    val builder: Builder
) : DefaultOkHttpClientFactory(builder) {
    override fun createBuilder(disableSslValidation: Boolean): Builder {
        if (disableSslValidation) {
            return super.createBuilder(disableSslValidation)
        }

        //TODO: remove usage of deprecated method
        return builder.sslSocketFactory(SSLContext.getDefault().socketFactory)
    }
}
