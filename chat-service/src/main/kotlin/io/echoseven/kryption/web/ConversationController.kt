package io.echoseven.kryption.web

import io.echoseven.kryption.domain.Conversation
import io.echoseven.kryption.domain.ConversationMessage
import io.echoseven.kryption.service.ConversationService
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
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

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteConversation(@PathVariable id: String) = conversationService.deleteConversation(id)

    @DeleteMapping("/message/{messageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteMessage(@PathVariable messageId: String) = conversationService.deleteMesage(messageId)
}
