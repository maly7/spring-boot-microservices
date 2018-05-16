package io.echoseven.kryption.web

import io.echoseven.kryption.domain.Chat
import io.echoseven.kryption.domain.ChatMessage
import io.echoseven.kryption.service.ChatService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/chat")
class ChatController(val chatService: ChatService) {

    @PostMapping
    fun sendMessage(@Valid @RequestBody chatMessage: ChatMessage) = chatService.sendMessage(chatMessage)

    // GET /chat/id - protect with method level security
    @GetMapping("/{id}")
    fun getChat(@PathVariable id: String): Chat = chatService.get(id)

    // DELETE /chat/id - protect with method level security
}
