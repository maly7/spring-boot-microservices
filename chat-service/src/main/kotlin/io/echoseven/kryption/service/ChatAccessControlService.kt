package io.echoseven.kryption.service

import io.echoseven.kryption.data.ChatRepository
import org.springframework.stereotype.Service

@Service
class ChatAccessControlService(val chatRepository: ChatRepository, val userService: UserService) {
    fun userInChat(chatId: String) =
        chatRepository.findByIdAndParticipantsContaining(chatId, userService.getCurrentUserId()).isPresent
}
