package io.echoseven.kryption.commons

import java.io.FileInputStream
import java.security.KeyStore
import javax.net.ssl.TrustManagerFactory
import javax.net.ssl.X509TrustManager

fun getTrustManager(trustStoreLocation: String, trustStorePassword: String): X509TrustManager {
    val keyStore = KeyStore.getInstance(KeyStore.getDefaultType())
    keyStore.load(FileInputStream(trustStoreLocation), trustStorePassword.toCharArray())

    val trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm())
    trustManagerFactory.init(keyStore)
    return trustManagerFactory.trustManagers.first { it is X509TrustManager } as X509TrustManager
}