package io.echoseven.kryption.service

import io.echoseven.kryption.data.ChatRepository
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class ChatAccessControlService(val chatRepository: ChatRepository, val userService: UserService) {
    private val log = LoggerFactory.getLogger(ChatAccessControlService::class.java)

    fun userInChat(chatId: String): Boolean {
        log.debug("Checking access to chat [{}] for user [{}]", chatId, userService.getCurrentUserId())
        return chatRepository.findByIdAndParticipantsContaining(chatId, userService.getCurrentUserId()).isPresent
    }
}
