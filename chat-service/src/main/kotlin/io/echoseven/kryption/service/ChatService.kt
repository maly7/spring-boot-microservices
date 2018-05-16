package io.echoseven.kryption.service

import io.echoseven.kryption.data.ChatMessageRepository
import io.echoseven.kryption.data.ChatRepository
import io.echoseven.kryption.domain.Chat
import io.echoseven.kryption.domain.ChatMessage
import io.echoseven.kryption.exception.BadRequestException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.Date

@Service
@Transactional
class ChatService(
    val userService: UserService,
    val chatRepository: ChatRepository,
    val chatMessageRepository: ChatMessageRepository
) {
    fun sendMessage(chatMessage: ChatMessage): Chat {
        if (chatMessage.toId == null) {
            throw BadRequestException("Chats must specify a to id")
        }

        chatMessage.fromId = userService.getCurrentUserId()
        chatMessage.timestamp = Date.from(Instant.now())
        val toId: String = chatMessage.toId!!
        val fromId = chatMessage.fromId!!

        val existingChatOpt = chatRepository.findByParticipantsContaining(toId, fromId)
        val existingChat = if (existingChatOpt.isPresent) {
            existingChatOpt.get()
        } else {
            create(fromId, toId)
        }

        existingChat.messages += chatMessageRepository.insert(chatMessage)
        return chatRepository.save(existingChat)
    }

    fun create(fromId: String, toId: String): Chat {
        if (userService.getCurrentUser().contacts.none { it.id == toId }) {
            throw BadRequestException("A User may not send contact to a user no in their contact list")
        }

        val chat = chatRepository.insert(Chat(participants = listOf(fromId, toId)))
        addChatToParticipants(chat)
        return chat
    }

    private fun addChatToParticipants(chat: Chat) {
        chat.participants.forEach { id ->
            val user = userService.get(id)
            user.chats += chat
            userService.save(user)
        }
    }
}
