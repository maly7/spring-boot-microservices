package com.github.maly7.service

import com.github.maly7.data.ConversationMessageRepository
import com.github.maly7.data.ConversationRepository
import com.github.maly7.exception.NotFoundException
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
