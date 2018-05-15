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

        val existingChat = chatRepository.findByParticipantsContaining(toId)
            .orElse(chatRepository.save(Chat(participants = listOf(fromId, toId))))

        existingChat.messages += chatMessageRepository.save(chatMessage)

        val savedChat = chatRepository.save(existingChat)
        addChatToParticipants(savedChat)
        return savedChat
    }

    private fun addChatToParticipants(chat: Chat) {
        chat.participants.forEach { id ->
            addChatToUser(id, chat)
        }
    }

    private fun addChatToUser(id: String, chat: Chat) {
        val user = userService.get(id)
        if (!user.chats.any { it.id == chat.id }) {
            user.chats += chat
            userService.save(user)
        }
    }
}
