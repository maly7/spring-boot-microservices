package io.echoseven.kryption.web

import io.echoseven.kryption.domain.Chat
import io.echoseven.kryption.domain.ChatMessage
import io.echoseven.kryption.service.ChatMessageService
import io.echoseven.kryption.service.ChatService
import org.springframework.http.HttpStatus
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
@RequestMapping("/chat")
class ChatController(val chatService: ChatService, val chatMessageService: ChatMessageService) {

    @PostMapping("/message")
    fun sendMessage(@Valid @RequestBody chatMessage: ChatMessage) = chatService.sendMessage(chatMessage)

    @GetMapping("/{id}")
    fun getChat(@PathVariable id: String): Chat = chatService.get(id)

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteChat(@PathVariable id: String) = chatService.delete(id)

    @DeleteMapping("/message/{messageId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteMessage(@PathVariable messageId: String) = chatMessageService.delete(messageId)
}
