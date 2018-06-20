package io.echoseven.kryption.web

import io.echoseven.kryption.domain.Conversation
import io.echoseven.kryption.domain.ConversationMessage
import io.echoseven.kryption.service.ConversationService
import io.echoseven.kryption.web.resource.DeleteResponse
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/conversation")
class ConversationController(val conversationService: ConversationService) {

    @PostMapping(
        path = ["/message"],
        produces = [MediaType.APPLICATION_JSON_UTF8_VALUE],
        consumes = [(MediaType.APPLICATION_JSON_UTF8_VALUE)]
    )
    fun sendMessage(@Valid @RequestBody conversationMessage: ConversationMessage) =
        conversationService.sendMessage(conversationMessage)

    @GetMapping(path = ["/{id}"], produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun getConversation(@PathVariable id: String): Conversation = conversationService.get(id)

    @DeleteMapping("/{id}", produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun deleteConversation(@PathVariable id: String): DeleteResponse {
        conversationService.deleteConversation(id)
        return DeleteResponse(id, "conversation")
    }

    @DeleteMapping("/message/{messageId}", produces = [MediaType.APPLICATION_JSON_UTF8_VALUE])
    fun deleteMessage(@PathVariable messageId: String): DeleteResponse {
        conversationService.deleteMessage(messageId)
        return DeleteResponse(messageId, "message")
    }
}
