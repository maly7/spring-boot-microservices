package io.echoseven.kryption.service

import io.echoseven.kryption.data.ChatMessageRepository
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.stereotype.Service

@Service
class ChatMessageService(val chatMessageRepository: ChatMessageRepository) {

    @PreAuthorize("@chatAccessControlService.userInMessage(#messageId)")
    fun delete(messageId: String) {
        chatMessageRepository.deleteById(messageId)
    }
}
