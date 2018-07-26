package io.echoseven.kryption.functional.support

import io.restassured.RestAssured
import org.junit.Before

open class FunctionalTest {

    private val baseUri = System.getProperty("funcTestUri", "https://localhost:8443")

    @Before
    open fun setup() {
        RestAssured.baseURI = baseUri
    }
}
