package io.echoseven.kryption.service

import io.echoseven.kryption.data.ChatMessageRepository
import io.echoseven.kryption.data.ChatRepository
import io.echoseven.kryption.exception.NotFoundException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ChatAccessControlService(
    val chatRepository: ChatRepository,
    val userService: UserService,
    val chatMessageRepository: ChatMessageRepository
) {
    private val log = LoggerFactory.getLogger(ChatAccessControlService::class.java)

    fun userInChat(chatId: String): Boolean {
        log.debug("Checking access to chat [{}] for user [{}]", chatId, userService.getCurrentUserId())
        return chatRepository.findByIdAndParticipantsContaining(chatId, userService.getCurrentUserId()).isPresent
    }

    fun userInMessage(messageId: String): Boolean {
        log.debug("Checking access to message [{}] for user [{}]", messageId, userService.getCurrentUserId())
        return chatMessageRepository.findById(messageId)
            .orElseThrow { NotFoundException("No message found for id $messageId") }
            .isParticipant(userService.getCurrentUserId())
    }
}
