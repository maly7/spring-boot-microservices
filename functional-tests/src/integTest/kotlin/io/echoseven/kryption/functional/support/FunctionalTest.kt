package io.echoseven.kryption.functional.support

import io.restassured.RestAssured
import org.junit.Before
import java.net.URI

open class FunctionalTest {

    private val baseUri = System.getProperty("funcTestUri", "http://localhost:8080")
    lateinit var host: String

    @Before
    open fun setup() {
        host = URI(baseUri).host
        RestAssured.baseURI = baseUri
    }
}
