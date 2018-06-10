package io.echoseven.kryption.functional.support

import org.springframework.messaging.simp.stomp.StompCommand
import org.springframework.messaging.simp.stomp.StompHeaders
import org.springframework.messaging.simp.stomp.StompSession
import org.springframework.messaging.simp.stomp.StompSessionHandlerAdapter
import java.lang.reflect.Type

class TestingStompSessionHandlerAdapter : StompSessionHandlerAdapter() {
    var messages: List<Any> = listOf()

    override fun getPayloadType(headers: StompHeaders): Type {
        return Any::class.java
    }

    override fun handleTransportError(session: StompSession, exception: Throwable) {
        throw exception
    }

    override fun handleException(
        session: StompSession,
        command: StompCommand?,
        headers: StompHeaders,
        payload: ByteArray,
        exception: Throwable
    ) {
        throw exception
    }

    override fun handleFrame(headers: StompHeaders, payload: Any?) {
        messages += payload!!
    }
}
