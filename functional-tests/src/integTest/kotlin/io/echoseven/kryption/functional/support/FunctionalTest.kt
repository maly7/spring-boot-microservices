package io.echoseven.kryption.functional.support

import io.restassured.RestAssured
import org.junit.Before

open class FunctionalTest {

    private val baseUri = System.getProperty("funcTestUri", "http://localhost:8080")

    @Before
    open fun setup() {
        RestAssured.baseURI = baseUri
    }
}