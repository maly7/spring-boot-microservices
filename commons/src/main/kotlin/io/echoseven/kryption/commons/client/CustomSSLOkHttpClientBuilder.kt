package io.echoseven.kryption.commons.client

import okhttp3.OkHttpClient
import org.springframework.cloud.commons.httpclient.DefaultOkHttpClientFactory
import javax.net.ssl.SSLContext

class CustomSSLOkHttpClientFactory(builder: okhttp3.OkHttpClient.Builder?) : DefaultOkHttpClientFactory(builder) {
    override fun createBuilder(disableSslValidation: Boolean): OkHttpClient.Builder {
        if (disableSslValidation) {
            return super.createBuilder(disableSslValidation)
        }

        //TODO: remove usage of deprecated method
        return OkHttpClient.Builder().sslSocketFactory(SSLContext.getDefault().socketFactory)
    }
}
