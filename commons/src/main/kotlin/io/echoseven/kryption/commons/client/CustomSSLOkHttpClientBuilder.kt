package io.echoseven.kryption.commons.client

import okhttp3.OkHttpClient.Builder
import org.springframework.cloud.commons.httpclient.DefaultOkHttpClientFactory
import java.io.FileInputStream
import java.security.KeyStore
import javax.net.ssl.SSLContext
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

class CustomSSLOkHttpClientFactory(
    val builder: Builder
) : DefaultOkHttpClientFactory(builder) {
    override fun createBuilder(disableSslValidation: Boolean): Builder {
        if (disableSslValidation) {
            return super.createBuilder(disableSslValidation)
        }

        val trustStore = System.getProperty("javax.net.ssl.trustStore")
        val trustStorePassword = System.getProperty("javax.net.ssl.trustStorePassword")
        val keyStore = KeyStore.getInstance("JKS")
        keyStore.load(FileInputStream(trustStore), trustStorePassword.toCharArray())

        val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
        trustManagerFactory.init(keyStore)
        val trustManager = trustManagerFactory.trustManagers.first { it is X509TrustManager } as X509TrustManager
        // TODO: try using commons-net?
        return builder.sslSocketFactory(SSLContext.getDefault().socketFactory, trustManager)
    }
}
