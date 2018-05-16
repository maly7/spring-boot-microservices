package io.echoseven.kryption.service

import io.echoseven.kryption.data.ChatMessageRepository
import io.echoseven.kryption.data.ChatRepository
import io.echoseven.kryption.domain.Chat
import io.echoseven.kryption.domain.ChatMessage
import io.echoseven.kryption.exception.BadRequestException
import io.echoseven.kryption.exception.NotFoundException
import org.slf4j.LoggerFactory
import org.springframework.security.access.prepost.PreAuthorize
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
    private val log = LoggerFactory.getLogger(ChatService::class.java)

    fun sendMessage(chatMessage: ChatMessage): Chat {
        if (chatMessage.toId == null) {
            throw BadRequestException("Chats must specify a to id")
        }

        chatMessage.fromId = userService.getCurrentUserId()
        chatMessage.timestamp = Date.from(Instant.now())
        val toId: String = chatMessage.toId!!
        val fromId = chatMessage.fromId!!

        log.debug("Attempting to send message from [{}] to [{}]", fromId, toId)

        val existingChatOpt = chatRepository.findByParticipantsContaining(toId, fromId)
        val existingChat = if (existingChatOpt.isPresent) {
            log.debug("Found chat between [{}] and [{}] with id [{}]", fromId, toId, existingChatOpt.get().id)
            existingChatOpt.get()
        } else {
            log.debug("Not existing chat found between [{}] and [{}], attempting to create one", fromId, toId)
            create(fromId, toId)
        }

        existingChat.messages += chatMessageRepository.insert(chatMessage)
        return chatRepository.save(existingChat)
    }

    fun create(fromId: String, toId: String): Chat {
        if (userService.getCurrentUser().contacts.none { it.id == toId }) {
            log.warn("User [{}] attempted to start chat with [{}], but they are not in their contacts", fromId, toId)
            throw BadRequestException("A User may not send contact to a user no in their contact list")
        }

        log.debug("Starting a new 1:1 chat between [{}] and [{}]", fromId, toId)
        val chat = chatRepository.insert(Chat(participants = listOf(fromId, toId)))
        addChatToParticipants(chat)
        return chat
    }

    @PreAuthorize("@chatAccessControlService.userInChat(#chatId)")
    fun get(chatId: String): Chat =
        chatRepository.findById(chatId).orElseThrow { NotFoundException("No Chat Found with id $chatId") }

    private fun addChatToParticipants(chat: Chat) {
        chat.participants.forEach { id ->
            log.debug("Adding [{}] to chat [{}]", id, chat.id)
            val user = userService.get(id)
            user.chats += chat
            userService.save(user)
        }
    }
}
