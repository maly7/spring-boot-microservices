package io.echoseven.kryption.service

import io.echoseven.kryption.data.ConversationMessageRepository
import io.echoseven.kryption.data.ConversationRepository
import io.echoseven.kryption.exception.NotFoundException
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class AccessControlService(
    val conversationRepository: ConversationRepository,
    val userService: UserService,
    val conversationMessageRepository: ConversationMessageRepository
) {
    private val log = LoggerFactory.getLogger(AccessControlService::class.java)

    fun userInConversation(conversationId: String): Boolean {
        log.debug("Checking access to conversation [{}] for user [{}]", conversationId, userService.getCurrentUserId())
        return conversationRepository.findByIdAndParticipantsContaining(conversationId, userService.getCurrentUserId()).isPresent
    }

    fun userInMessage(messageId: String): Boolean {
        log.debug("Checking access to message [{}] for user [{}]", messageId, userService.getCurrentUserId())
        return conversationMessageRepository.findById(messageId)
            .orElseThrow { NotFoundException("No message found for id $messageId") }
            .isParticipant(userService.getCurrentUserId())
    }
}
