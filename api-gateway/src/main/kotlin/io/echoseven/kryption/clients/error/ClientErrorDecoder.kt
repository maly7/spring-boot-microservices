package io.echoseven.kryption.clients.error

import feign.Response
import feign.codec.ErrorDecoder
import io.echoseven.kryption.exception.ClientErrorException
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.web.server.ServerErrorException
import java.lang.Exception

class ClientErrorDecoder : ErrorDecoder {
    private val log = LoggerFactory.getLogger(ClientErrorDecoder::class.java)

    override fun decode(methodKey: String, response: Response): Exception {
        val status = response.status()
        log.debug("Handling error for [{}] with response status [{}]", methodKey, status)

        if (status == 0 || HttpStatus.valueOf(status).is5xxServerError) {
            return ServerErrorException("Exception occurred during processing request for $methodKey", null)
        }

        return ClientErrorException(HttpStatus.valueOf(status), "Client Error occurred during request for $methodKey")
    }
}
