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
        guardAgainstBadMessages(chatMessage)

        chatMessage.fromId = userService.getCurrentUserId()
        chatMessage.timestamp = Date.from(Instant.now())
        val toId: String = chatMessage.toId!!
        val fromId = chatMessage.fromId!!

        val existingChat = chatRepository.findByParticipantsContaining(toId)
            .orElse(chatRepository.insert(Chat(participants = listOf(fromId, toId))))

        existingChat.messages += chatMessageRepository.insert(chatMessage)

        val savedChat = chatRepository.save(existingChat)
        addChatToParticipants(savedChat)
        return savedChat
    }

    private fun guardAgainstBadMessages(chatMessage: ChatMessage) {
        if (chatMessage.toId == null) {
            throw BadRequestException("Chats must specify a to id")
        }

        if (userService.getCurrentUser().contacts.none { it.id == chatMessage.toId }) {
            throw BadRequestException("A User may not send contact to a user no in their contact list")
        }
    }

    private fun addChatToParticipants(chat: Chat) {
        chat.participants.forEach { id ->
            addChatToUser(id, chat)
        }
    }

    private fun addChatToUser(id: String, chat: Chat) {
        val user = userService.get(id)
        if (user.chats.none { it.id == chat.id }) {
            user.chats += chat
            userService.save(user)
        }
    }
}
