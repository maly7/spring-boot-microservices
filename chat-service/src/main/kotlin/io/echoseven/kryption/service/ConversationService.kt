package io.echoseven.kryption.service

import io.echoseven.kryption.data.ConversationMessageRepository
import io.echoseven.kryption.data.ConversationRepository
import io.echoseven.kryption.domain.Conversation
import io.echoseven.kryption.domain.ConversationMessage
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
class ConversationService(
    val userService: UserService,
    val notificationService: NotificationService,
    val conversationRepository: ConversationRepository,
    val conversationMessageRepository: ConversationMessageRepository
) {
    private val log = LoggerFactory.getLogger(ConversationService::class.java)

    fun sendMessage(conversationMessage: ConversationMessage): Conversation {
        if (conversationMessage.toId == null) {
            throw BadRequestException("Conversation messages must specify a to id")
        }

        conversationMessage.fromId = userService.getCurrentUserId()
        conversationMessage.timestamp = Date.from(Instant.now())
        val toId: String = conversationMessage.toId!!
        val fromId = conversationMessage.fromId!!

        log.debug("Attempting to send message from [{}] to [{}]", fromId, toId)
        val existingConversation = getOrCreateConversationForUsers(fromId, toId)

        val insertedMessage = conversationMessageRepository.insert(conversationMessage)
        existingConversation.messages += insertedMessage

        val conversation = conversationRepository.save(existingConversation)
        notificationService.notifyNewMessage(toId, conversation.id!!, insertedMessage)

        return conversation
    }

    @PreAuthorize("@accessControlService.userInConversation(#conversationId)")
    fun get(conversationId: String): Conversation =
        conversationRepository.findById(conversationId).orElseThrow { NotFoundException("No Conversation Found with id $conversationId") }

    @PreAuthorize("@accessControlService.userInConversation(#conversationId)")
    fun deleteConversation(conversationId: String) {
        val conversation = conversationRepository.findById(conversationId)
            .orElseThrow { NotFoundException("No conversation found with id $conversationId") }

        conversationMessageRepository.deleteAll(conversation.messages)
        conversationRepository.deleteById(conversationId)
        notificationService.notifyConversationDelete(conversation.participants, conversationId)

        log.debug("User [{}] deleted conversation [{}]", userService.getCurrentUserId(), conversationId)
    }

    @PreAuthorize("@accessControlService.userInMessage(#messageId)")
    fun deleteMessage(messageId: String) {
        val message = conversationMessageRepository.findById(messageId)
            .orElseThrow { NotFoundException("No message found for id $messageId") }
        val conversation = conversationRepository.findByMessagesContaining(message)
            .orElseThrow { IllegalStateException("No conversation found for existing message") }

        conversationMessageRepository.deleteById(messageId)
        log.debug("User [{}] deleted message [{}]", userService.getCurrentUserId(), messageId)

        if (conversation.messages.size <= 1) {
            log.debug("Conversation [{}] should now be empty, deleting it", conversation.id)

            conversationRepository.delete(conversation)
            notificationService.notifyConversationDelete(conversation.participants, conversation.id!!)
        } else {
            notificationService.notifyMessageDelete(conversation.participants, conversation.id!!, messageId)
        }
    }

    private fun addConversationToParticipants(conversation: Conversation) {
        conversation.participants.forEach { id ->
            log.debug("Adding [{}] to conversation [{}]", id, conversation.id)
            val user = userService.get(id)
            user.conversations += conversation
            userService.save(user)
        }
    }

    private fun getOrCreateConversationForUsers(fromId: String, toId: String): Conversation {
        val existingConversationOpt = conversationRepository.findByParticipantsContaining(toId, fromId)
        return if (existingConversationOpt.isPresent) {
            log.debug(
                "Found conversation between [{}] and [{}] with id [{}]",
                fromId,
                toId,
                existingConversationOpt.get().id
            )
            existingConversationOpt.get()
        } else {
            log.debug(
                "No existing conversation found between [{}] and [{}], attempting to create one",
                fromId,
                toId
            )
            create(fromId, toId)
        }
    }

    private fun create(fromId: String, toId: String): Conversation {
        if (userService.getCurrentUser().contacts.none { it.id == toId }) {
            log.warn(
                "User [{}] attempted to start conversation with [{}], but they are not in their contacts",
                fromId,
                toId
            )
            throw BadRequestException("A User may not send contact to a user no in their contact list")
        }

        log.debug("Starting a new 1:1 conversation between [{}] and [{}]", fromId, toId)
        val conversation = conversationRepository.insert(Conversation(participants = listOf(fromId, toId)))
        addConversationToParticipants(conversation)
        return conversation
    }
}
